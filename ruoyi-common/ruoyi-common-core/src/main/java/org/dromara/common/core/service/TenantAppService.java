package org.dromara.common.core.service;

/**
 * 通用租户应用服务
 *
 * @author hexm
 * @date 2023/5/17
 */
public interface TenantAppService {

    /**
     * 查询appKey对应的租户id
     *
     * @param appKey 应用key
     * @return 租户id
     */
    String getTenantIdByAppKey(String appKey);
}
