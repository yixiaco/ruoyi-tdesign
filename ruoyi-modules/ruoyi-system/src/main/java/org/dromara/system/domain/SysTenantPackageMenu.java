package org.dromara.system.domain;

import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * 租户套餐和菜单关联对象 sys_tenant_package_menu
 *
 * @author hexm
 * @date 2023-11-08
 */
@Data
@Table("sys_tenant_package_menu")
public class SysTenantPackageMenu implements Serializable {

    /**
     * 租户套餐id
     */
    private Long packageId;

    /**
     * 菜单id
     */
    private Long menuId;

}
