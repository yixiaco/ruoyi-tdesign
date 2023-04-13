package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysTenantPackage;
import org.dromara.system.domain.query.SysTenantPackageQuery;
import org.dromara.system.domain.vo.SysTenantPackageVo;

import java.util.List;

/**
 * 租户套餐Mapper接口
 *
 * @author Michelle.Chung
 */
public interface SysTenantPackageMapper extends BaseMapperPlus<SysTenantPackage, SysTenantPackageVo> {

    /**
     * 查询租户套餐列表
     *
     * @param query 查询对象
     * @return {@link SysTenantPackageVo}
     */
    List<SysTenantPackageVo> queryList(SysTenantPackageQuery query);
}
