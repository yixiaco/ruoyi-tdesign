package org.dromara.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.dromara.system.domain.SysTenantPackageMenu;

import java.util.List;


/**
 * 租户套餐和菜单关联Service接口
 *
 * @author hexm
 * @date 2023-11-08
 */
public interface ISysTenantPackageMenuService extends IService<SysTenantPackageMenu> {

    /**
     * 更新租户套餐与菜单关联
     *
     * @param packageId 租户id
     * @param menuIds   菜单id
     */
    void add(Long packageId, List<Long> menuIds);

    /**
     * 更新租户套餐与菜单关联
     *
     * @param packageId 租户id
     * @param menuIds   菜单id
     */
    void update(Long packageId, List<Long> menuIds);
}
