package org.dromara.system.mapper;

import org.apache.ibatis.annotations.Param;
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

    /**
     * 查询租户套餐详情
     *
     * @param packageId 租户套餐id
     * @return
     */
    SysTenantPackageVo queryById(@Param("packageId") Long packageId);

    /**
     * 是否包含分配菜单
     *
     * @param menuId 菜单id
     * @return
     */
    boolean queryIncludeMenuId(@Param("menuId") Long menuId);
}
