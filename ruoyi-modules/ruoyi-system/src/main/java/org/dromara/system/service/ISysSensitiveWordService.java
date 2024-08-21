package org.dromara.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysSensitiveWord;
import org.dromara.system.domain.bo.SysSensitiveWordBo;
import org.dromara.system.domain.query.SysSensitiveWordQuery;
import org.dromara.system.domain.vo.SysSensitiveWordVo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 敏感词Service接口
 *
 * @author hexm
 * @date 2024-08-16
 */
public interface ISysSensitiveWordService extends IService<SysSensitiveWord> {

    /**
     * 查询敏感词
     *
     * @param wordId 主键
     * @return SysSensitiveWordVo
     */
    SysSensitiveWordVo queryById(Long wordId);

    /**
     * 查询敏感词列表
     *
     * @param query 查询对象
     * @return SysSensitiveWordVo
     */
    TableDataInfo<SysSensitiveWordVo> queryPageList(SysSensitiveWordQuery query);

    /**
     * 查询敏感词列表
     *
     * @param query 查询对象
     * @return SysSensitiveWordVo
     */
    List<SysSensitiveWordVo> queryList(SysSensitiveWordQuery query);

    /**
     * 新增敏感词
     *
     * @param bo           敏感词新增业务对象
     * @return Boolean
     */
    Boolean insertByBo(SysSensitiveWordBo bo);

    /**
     * 新增敏感词
     *
     * @param bo           敏感词新增业务对象
     * @param refreshCache 刷新缓存
     * @return Boolean
     */
    Boolean insertByBo(SysSensitiveWordBo bo, boolean refreshCache);

    /**
     * 修改敏感词
     *
     * @param bo           敏感词编辑业务对象
     * @return Boolean
     */
    Boolean updateByBo(SysSensitiveWordBo bo);

    /**
     * 修改敏感词
     *
     * @param bo           敏感词编辑业务对象
     * @param refreshCache 刷新缓存
     * @return Boolean
     */
    Boolean updateByBo(SysSensitiveWordBo bo, boolean refreshCache);

    /**
     * 校验并批量删除敏感词信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);

    /**
     * 刷新敏感词缓存数据
     */
    void refreshCache();

    /**
     * 初始化敏感词替换器
     */
    void initSensitiveWordReplacers();

    /**
     * 判断是否包含敏感词
     *
     * @param text     文本
     * @param category 敏感词分类
     * @return 是否包含敏感词
     */
    boolean containsSensitiveWord(String text, String... category);

    /**
     * 敏感词替换
     *
     * @param text     文本
     * @param category 敏感词分类
     * @return 替换后的文本
     */
    String sensitiveWordReplace(String text, String... category);

    /**
     * 查找敏感词，返回找到的所有敏感词
     *
     * @param text     文本
     * @param category 敏感词分类
     * @return 敏感词
     */
    Set<String> getFoundAllSensitive(String text, String... category);
}
