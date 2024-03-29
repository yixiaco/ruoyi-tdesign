package org.dromara.common.tenant.factory;

import com.mybatisflex.core.tenant.TenantFactory;

/**
 * 全局多租户配置
 *
 * @author hexm
 * @date 2024/03/29 17:26
 */
public class GlobalTenantFactory implements TenantFactory {
    /**
     * @deprecated 使用 {@link #getTenantIds(String)} 代替。
     */
    @Override
    public Object[] getTenantIds() {
        return new Object[0];
    }

    @Override
    public Object[] getTenantIds(String tableName) {
        return TenantFactory.super.getTenantIds(tableName);
    }
}
