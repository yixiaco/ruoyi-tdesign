package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 租户查询对象 sys_tenant
 *
 * @author hexm
 * @date 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysTenantQuery extends BaseEntity {

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 联系人
     */
    private String contactUserName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 统一社会信用代码
     */
    private String licenseNumber;

    /**
     * 地址
     */
    private String address;

    /**
     * 域名
     */
    private String domain;

    /**
     * 租户套餐编号
     */
    private Long packageId;

    /**
     * 租户状态
     */
    private String status;

}
