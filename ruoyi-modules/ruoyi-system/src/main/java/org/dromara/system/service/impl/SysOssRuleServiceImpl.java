package org.dromara.system.service.impl;

import cn.dev33.satoken.context.SaHolder;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.dromara.common.core.constant.CacheConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.OssRuleService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.spring.SpringExpressionUtil;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.redis.utils.RedisLockUtil;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.system.domain.SysOssRule;
import org.dromara.system.domain.bo.SysOssRuleBo;
import org.dromara.system.domain.query.SysOssRuleQuery;
import org.dromara.system.domain.vo.SysOssRuleVo;
import org.dromara.system.enums.YesNoEnum;
import org.dromara.system.mapper.SysOssRuleMapper;
import org.dromara.system.service.ISysOssRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * OSS处理规则Service业务层处理
 *
 * @author hexm
 * @date 2023-05-05
 */
@Service
public class SysOssRuleServiceImpl extends ServiceImpl<SysOssRuleMapper, SysOssRule> implements ISysOssRuleService, OssRuleService {

    /**
     * 查询OSS处理规则
     *
     * @param ossRuleId 主键
     * @return SysOssRuleVo
     */
    @Override
    public SysOssRuleVo queryById(Long ossRuleId) {
        return baseMapper.selectVoById(ossRuleId);
    }

    /**
     * 查询OSS处理规则列表
     *
     * @param query 查询对象
     * @return SysOssRuleVo
     */
    @Override
    public TableDataInfo<SysOssRuleVo> queryPageList(SysOssRuleQuery query) {
        return PageQuery.of(() -> baseMapper.queryList(query));
    }

    /**
     * 查询OSS处理规则列表
     *
     * @param query 查询对象
     * @return SysOssRuleVo
     */
    @Override
    public List<SysOssRuleVo> queryList(SysOssRuleQuery query) {
        return baseMapper.queryList(query);
    }

    /**
     * 根据新增业务对象插入OSS处理规则
     *
     * @param bo OSS处理规则新增业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SysOssRuleBo bo) {
        checkRepeat(bo);
        SysOssRule add = MapstructUtils.convert(bo, SysOssRule.class);
        boolean save = save(add);
        removeCache();
        return save;
    }

    /**
     * 根据编辑业务对象修改OSS处理规则
     *
     * @param bo OSS处理规则编辑业务对象
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(SysOssRuleBo bo) {
        checkRepeat(bo);
        SysOssRule update = MapstructUtils.convert(bo, SysOssRule.class);
        boolean b = updateById(update);
        removeCache();
        return b;
    }

    /**
     * 校验并批量删除OSS处理规则信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        boolean b = removeByIds(ids);
        removeCache();
        return b;
    }

    /**
     * 删除缓存
     */
    @Override
    public void removeCache() {
        RedisUtils.deleteObject(CacheConstants.SYS_OSS_RULE);
    }

    /**
     * 规则默认值修改
     *
     * @param ossRuleId 规则id
     * @param isDefault 默认值
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDefault(Long ossRuleId, String isDefault) {
        SysOssRule rule = getById(ossRuleId);
        if (YesNoEnum.YES.getCode().equals(isDefault)) {
            // 设置其他相同域名的规则为非默认
            lambdaUpdate().eq(SysOssRule::getDomain, rule.getDomain())
                .set(SysOssRule::getIsDefault, YesNoEnum.NO.getCode())
                .update();
        }
        // 设置当前规则
        lambdaUpdate().eq(SysOssRule::getOssRuleId, ossRuleId)
            .set(SysOssRule::getIsDefault, isDefault)
            .update();
        removeCache();
    }

    /**
     * 检查重复规则
     *
     * @param bo 业务对象
     */
    private void checkRepeat(SysOssRuleBo bo) {
        boolean exists = lambdaQuery()
            .ne(bo.getOssRuleId() != null, SysOssRule::getOssRuleId, bo.getOssRuleId())
            .eq(SysOssRule::getRuleName, bo.getRuleName())
            .eq(SysOssRule::getDomain, bo.getDomain())
            .exists();
        if (exists) {
            throw new ServiceException("该域名下已存在相同的规则");
        }
    }

    /**
     * 返回多字段url
     *
     * @param fieldName   字段名称
     * @param originalUrl 原始url
     * @param useRules    限定使用规则，为空则不限制
     * @return
     */
    @Override
    @SuppressWarnings("unchecked cast")
    public Map<String, String> getUrls(String fieldName, String originalUrl, String[] useRules) {
        // 二级缓存
        List<SysOssRule> rules = (List<SysOssRule>) SaHolder.getStorage().get(CacheConstants.SYS_OSS_RULE);
        if (rules == null) {
            rules = RedisLockUtil.getOrSaveList(CacheConstants.SYS_OSS_RULE, () ->
                lambdaQuery().eq(SysOssRule::getStatus, "0").list()
            );
            SaHolder.getStorage().set(CacheConstants.SYS_OSS_RULE, rules);
        }
        Map<String, String> result = new LinkedHashMap<>();
        // 多个url拆分
        String[] urls = splitUrl(originalUrl);
        for (String url : urls) {
            String mimeType = HttpUtil.getMimeType(originalUrl);
            List<SysOssRule> list = rules.stream().filter(sysOssRule ->
                mimeType.contains(sysOssRule.getMimeType())
                    && originalUrl.contains(sysOssRule.getDomain())
                    && (useRules == null || ArrayUtil.contains(useRules, sysOssRule.getRuleName()))
            ).toList();

            // 设置默认字段值
            Optional<SysOssRule> defaultRuleOpt = StreamUtils.findFirst(list, o -> YesNoEnum.YES.getCode().equals(o.getIsDefault()));
            String realUrl = url;
            // 如果存在默认规则，则覆盖默认值
            if (defaultRuleOpt.isPresent()) {
                realUrl = getRealUrl(defaultRuleOpt.get().getRule(), url);
            }
            result.merge(fieldName, realUrl, (s, s2) -> String.join(",", s, s2));

            // 设置规则值
            List<SysOssRule> noDefaultRule = list
                .stream()
                .filter(ossRule -> YesNoEnum.NO.getCode().equals(ossRule.getIsDefault()))
                .toList();
            for (SysOssRule rule : noDefaultRule) {
                String key = fieldName + "_" + rule.getRuleName();
                realUrl = getRealUrl(rule.getRule(), url);
                result.merge(key, realUrl, (s, s2) -> String.join(",", s, s2));
            }
        }
        return result;
    }

    /**
     * 得到渲染后的url
     *
     * @param rule 规则
     * @param url  原始url
     * @return
     */
    private String getRealUrl(String rule, String url) {
        // 解析url
        UrlBuilder builder = UrlBuilder.ofHttp(url, CharsetUtil.CHARSET_UTF_8);
        HashMap<String, Object> variable = new HashMap<>();
        String domain = builder.getScheme() + "://" + builder.getHost();
        if (builder.getPort() > 0) {
            domain += ":" + builder.getPort();
        }
        variable.put("domain", domain);
        List<String> segments = builder.getPath().getSegments();
        variable.put("path", String.join("/", segments.subList(0, segments.size() - 1)));
        variable.put("filename", segments.get(segments.size() - 1));
        variable.put("url", url);
        return SpringExpressionUtil.parseExpression(rule, variable);
    }

    /**
     * 对原始url拆分
     *
     * @param url 原始url
     * @return
     */
    private String[] splitUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return new String[0];
        }
        return Arrays.stream(url.split(",http")).map(u -> {
            if (!u.startsWith("http")) {
                return "http" + u;
            }
            return u;
        }).toArray(String[]::new);
    }
}
