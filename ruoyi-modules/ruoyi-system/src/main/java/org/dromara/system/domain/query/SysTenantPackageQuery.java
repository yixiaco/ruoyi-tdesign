package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 租户套餐查询对象 sys_tenant_package
 *
 * @author hexm
 * @date 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysTenantPackageQuery extends BaseEntity {

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 状态（1正常 0停用）
     */
    private String status;

}
