package org.dromara.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.system.domain.SysTenantPackageMenu;

import java.util.List;


/**
 * 租户套餐和菜单关联Mapper接口
 *
 * @author hexm
 * @date 2023-11-08
 */
public interface SysTenantPackageMenuMapper extends BaseMapper<SysTenantPackageMenu> {

    /**
     * 删除租户角色多余菜单
     *
     * @param packageId 套餐id
     * @param menuIds   菜单id
     */
    void deleteTenantRoleMenuId(@Param("packageId") Long packageId, @Param("menuIds") List<Long> menuIds);

    /**
     * 添加租户管理员角色新增菜单
     *
     * @param packageId 套餐id
     * @param menuIds   菜单id
     */
    void addTenantRoleAdminMenuId(@Param("packageId") Long packageId, @Param("menuIds") List<Long> menuIds);
}
