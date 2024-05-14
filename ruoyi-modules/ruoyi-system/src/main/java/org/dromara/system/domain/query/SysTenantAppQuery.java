package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BasePageQuery;

/**
 * 租户应用管理查询对象 sys_tenant_app
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysTenantAppQuery extends BasePageQuery {

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 应用key
     */
    private String appKey;

    /**
     * 应用名称
     */
    private String appName;

}
