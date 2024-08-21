package org.dromara.system.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.dfa.FoundWord;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.dromara.common.core.constant.CacheConstants;
import org.dromara.common.core.enums.NormalDisableEnum;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.sensitive.SensitiveWordReplacer;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.redis.utils.RedisLockUtil;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.system.domain.SysSensitiveWord;
import org.dromara.system.domain.bo.SysSensitiveWordBo;
import org.dromara.system.domain.query.SysSensitiveWordQuery;
import org.dromara.system.domain.vo.SysSensitiveWordVo;
import org.dromara.system.listener.SensitiveWordRedisMQListener;
import org.dromara.system.mapper.SysSensitiveWordMapper;
import org.dromara.system.service.ISysSensitiveWordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 敏感词Service业务层处理
 *
 * @author hexm
 * @date 2024-08-16
 */
@Getter
@Service
public class SysSensitiveWordServiceImpl extends ServiceImpl<SysSensitiveWordMapper, SysSensitiveWord> implements ISysSensitiveWordService {

    private volatile SensitiveWordReplacer allSensitiveWordReplacer = new SensitiveWordReplacer(Collections.emptySet());
    /**
     * 标签与敏感词的字段数的映射
     */
    private volatile Map<String, SensitiveWordReplacer> categorySensitiveWordReplacers = Collections.emptyMap();

    /**
     * 查询敏感词
     *
     * @param wordId 主键
     * @return SysSensitiveWordVo
     */
    @Override
    public SysSensitiveWordVo queryById(Long wordId) {
        return baseMapper.selectVoById(wordId);
    }

    /**
     * 查询敏感词列表
     *
     * @param query 查询对象
     * @return SysSensitiveWordVo
     */
    @Override
    public TableDataInfo<SysSensitiveWordVo> queryPageList(SysSensitiveWordQuery query) {
        return PageQuery.of(() -> baseMapper.queryList(query));
    }

    /**
     * 查询敏感词列表
     *
     * @param query 查询对象
     * @return SysSensitiveWordVo
     */
    @Override
    public List<SysSensitiveWordVo> queryList(SysSensitiveWordQuery query) {
        return baseMapper.queryList(query);
    }

    /**
     * 新增敏感词
     *
     * @param bo 敏感词新增业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysSensitiveWordBo bo) {
        return insertByBo(bo, true);
    }

    /**
     * 根据新增业务对象插入敏感词
     *
     * @param bo           敏感词新增业务对象
     * @param refreshCache 刷新缓存
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysSensitiveWordBo bo, boolean refreshCache) {
        saveValid(bo);
        SysSensitiveWord add = MapstructUtils.convert(bo, SysSensitiveWord.class);
        boolean save = save(add);
        if (save && refreshCache) {
            refreshCache();
        }
        return save;
    }

    /**
     * 修改敏感词
     *
     * @param bo 敏感词编辑业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysSensitiveWordBo bo) {
        return updateByBo(bo, true);
    }

    /**
     * 根据编辑业务对象修改敏感词
     *
     * @param bo           敏感词编辑业务对象
     * @param refreshCache 刷新缓存
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysSensitiveWordBo bo, boolean refreshCache) {
        saveValid(bo);
        SysSensitiveWord word = getById(bo.getWordId());
        SysSensitiveWord update = MapstructUtils.convert(bo, SysSensitiveWord.class);
        boolean b = updateById(update);
        if (b && refreshCache) {
            // 只有改变以下数据的值才刷新缓存
            if (!Objects.equals(word.getWord(), bo.getWord())
                || !Objects.equals(word.getCategory(), bo.getCategory())
                || !Objects.equals(word.getStatus(), bo.getStatus())) {
                refreshCache();
            }
        }
        return b;
    }

    /**
     * 保存前校验数据
     *
     * @param bo 敏感词编辑业务对象
     */
    private void saveValid(SysSensitiveWordBo bo) {
        boolean exists = lambdaQuery()
            .ne(bo.getWordId() != null, SysSensitiveWord::getWordId, bo.getWordId())
            .eq(SysSensitiveWord::getWord, bo.getWord())
            .exists();
        if (exists) {
            throw new ServiceException("敏感词已存在");
        }
    }

    /**
     * 校验并批量删除敏感词信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        boolean b = removeByIds(ids);
        if (b) {
            refreshCache();
        }
        return b;
    }

    /**
     * 获取敏感词缓存数据
     */
    private Map<String, Set<String>> getCacheMap() {
        return RedisLockUtil.getOrSaveMap(CacheConstants.SYS_SENSITIVE_WORD, () -> {
            List<SysSensitiveWord> list = lambdaQuery()
                .eq(SysSensitiveWord::getStatus, NormalDisableEnum.NORMAL.getCodeNum())
                .select(SysSensitiveWord::getCategory, SysSensitiveWord::getWord)
                .list();
            return list
                .stream()
                .collect(Collectors.groupingBy(
                    SysSensitiveWord::getCategory,
                    HashMap::new,
                    Collectors.mapping(SysSensitiveWord::getWord, Collectors.toSet())));
        });
    }

    /**
     * 刷新敏感词缓存数据
     */
    @Override
    public void refreshCache() {
        RedisUtils.deleteObject(CacheConstants.SYS_SENSITIVE_WORD);
        RedisUtils.publish(SensitiveWordRedisMQListener.CHANNEL_KEY, "");
    }

    /**
     * 初始化敏感词替换器
     */
    @PostConstruct
    @Override
    public void initSensitiveWordReplacers() {
        Map<String, Set<String>> cacheMap = getCacheMap();
        // 全部替换器
        allSensitiveWordReplacer = new SensitiveWordReplacer(cacheMap.values().stream()
            .flatMap(Collection::stream).collect(Collectors.toSet()));
        // 分类替换器
        categorySensitiveWordReplacers = new HashMap<>();
        cacheMap.forEach((category, sensitiveWords) -> {
            SensitiveWordReplacer sensitiveWordReplacer = new SensitiveWordReplacer(sensitiveWords);
            categorySensitiveWordReplacers.put(category, sensitiveWordReplacer);
        });
    }

    /**
     * 判断是否包含敏感词
     *
     * @param text     文本
     * @param category 敏感词分类
     * @return 是否包含敏感词
     */
    @Override
    public boolean containsSensitiveWord(String text, String... category) {
        if (ArrayUtil.isNotEmpty(category)) {
            for (String c : category) {
                SensitiveWordReplacer sensitiveWordReplacer = categorySensitiveWordReplacers.get(c);
                if (sensitiveWordReplacer != null && sensitiveWordReplacer.containsSensitiveWord(text)) {
                    return true;
                }
            }
            return false;
        } else {
            return allSensitiveWordReplacer.containsSensitiveWord(text);
        }
    }

    /**
     * 敏感词替换
     *
     * @param text     文本
     * @param category 敏感词分类
     * @return 替换后的文本
     */
    @Override
    public String sensitiveWordReplace(String text, String... category) {
        if (ArrayUtil.isNotEmpty(category)) {
            for (String c : category) {
                SensitiveWordReplacer sensitiveWordReplacer = categorySensitiveWordReplacers.get(c);
                if (sensitiveWordReplacer != null) {
                    text = sensitiveWordReplacer.sensitiveWordReplace(text);
                }
            }
            return text;
        } else {
            return allSensitiveWordReplacer.sensitiveWordReplace(text);
        }
    }

    /**
     * 查找敏感词，返回找到的所有敏感词
     *
     * @param text     文本
     * @param category 敏感词分类
     * @return 敏感词
     */
    @Override
    public Set<String> getFoundAllSensitive(String text, String... category) {
        List<FoundWord> list;
        if (ArrayUtil.isNotEmpty(category)) {
            list = new ArrayList<>();
            for (String c : category) {
                SensitiveWordReplacer sensitiveWordReplacer = categorySensitiveWordReplacers.get(c);
                if (sensitiveWordReplacer != null) {
                    list.addAll(sensitiveWordReplacer.getFoundAllSensitive(text, true, true));
                }
            }
        } else {
            list = allSensitiveWordReplacer.getFoundAllSensitive(text, true, true);
        }
        return StreamUtils.toSet(list, FoundWord::getWord);
    }
}
