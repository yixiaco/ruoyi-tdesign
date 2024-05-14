package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysTenantApp;
import org.dromara.system.domain.query.SysTenantAppQuery;
import org.dromara.system.domain.vo.SysTenantAppVo;

import java.util.List;

/**
 * 租户应用管理Mapper接口
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
public interface SysTenantAppMapper extends BaseMapperPlus<SysTenantApp, SysTenantAppVo> {

    /**
     * 查询租户应用管理列表
     *
     * @param query 查询对象
     * @return {@link SysTenantAppVo}
     */
    List<SysTenantAppVo> queryList(SysTenantAppQuery query);
}
