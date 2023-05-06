package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysOssRule;
import org.dromara.system.domain.query.SysOssRuleQuery;
import org.dromara.system.domain.vo.SysOssRuleVo;

import java.util.List;

/**
 * OSS处理规则Mapper接口
 *
 * @author hexm
 * @date 2023-05-05
 */
public interface SysOssRuleMapper extends BaseMapperPlus<SysOssRule, SysOssRuleVo> {

    /**
     * 查询OSS处理规则列表
     *
     * @param query 查询对象
     * @return {@link SysOssRuleVo}
     */
    List<SysOssRuleVo> queryList(SysOssRuleQuery query);
}
