package org.dromara.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.SysOssRule;
import org.dromara.system.domain.bo.SysOssRuleBo;
import org.dromara.system.domain.query.SysOssRuleQuery;
import org.dromara.system.domain.vo.SysOssRuleVo;

import java.util.Collection;
import java.util.List;

/**
 * OSS处理规则Service接口
 *
 * @author hexm
 * @date 2023-05-05
 */
public interface ISysOssRuleService extends IService<SysOssRule> {

    /**
     * 查询OSS处理规则
     *
     * @param ossRuleId 主键
     * @return SysOssRuleVo
     */
    SysOssRuleVo queryById(Long ossRuleId);

    /**
     * 查询OSS处理规则列表
     *
     * @param query 查询对象
     * @return SysOssRuleVo
     */
    TableDataInfo<SysOssRuleVo> queryPageList(SysOssRuleQuery query);

    /**
     * 查询OSS处理规则列表
     *
     * @param query 查询对象
     * @return SysOssRuleVo
     */
    List<SysOssRuleVo> queryList(SysOssRuleQuery query);

    /**
     * 新增OSS处理规则
     *
     * @param bo OSS处理规则新增业务对象
     * @return Boolean
     */
    Boolean insertByBo(SysOssRuleBo bo);

    /**
     * 修改OSS处理规则
     *
     * @param bo OSS处理规则编辑业务对象
     * @return Boolean
     */
    Boolean updateByBo(SysOssRuleBo bo);

    /**
     * 校验并批量删除OSS处理规则信息
     *
     * @param ids 主键集合
     * @return Boolean
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);

    /**
     * 删除缓存
     */
    void removeCache();

    /**
     * 规则覆盖字段值修改
     *
     * @param ossRuleId   规则id
     * @param isOverwrite 是否覆盖字段值
     */
    void updateOverwrite(Long ossRuleId, String isOverwrite);
}
