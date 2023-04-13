package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysTenant;
import org.dromara.system.domain.query.SysTenantQuery;
import org.dromara.system.domain.vo.SysTenantVo;

import java.util.List;

/**
 * 租户Mapper接口
 *
 * @author Michelle.Chung
 */
public interface SysTenantMapper extends BaseMapperPlus<SysTenant, SysTenantVo> {

    /**
     * 查询租户列表
     *
     * @param query 查询对象
     * @return {@link SysTenantVo}
     */
    List<SysTenantVo> queryList(SysTenantQuery query);
}
