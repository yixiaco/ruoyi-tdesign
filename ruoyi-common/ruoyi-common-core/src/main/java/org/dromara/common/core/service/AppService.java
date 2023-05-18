package org.dromara.common.core.service;

/**
 * 通用应用服务
 *
 * @author hexm
 * @date 2023/5/17
 */
public interface AppService {

    /**
     * 查询appKey对应的租户id
     *
     * @param appKey
     * @return
     */
    String getTenantIdByAppKey(String appKey);
}
