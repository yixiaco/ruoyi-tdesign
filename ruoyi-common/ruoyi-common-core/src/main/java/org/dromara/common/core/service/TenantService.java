package org.dromara.common.core.service;

/**
 * 通用 租户服务
 *
 * @author hexm
 */
public interface TenantService {

    /**
     * 通过租户ID查询租户企业名称
     *
     * @param tenantId 租户ID
     * @return 租户名称
     */
    String selectTenantNameById(String tenantId);

}
