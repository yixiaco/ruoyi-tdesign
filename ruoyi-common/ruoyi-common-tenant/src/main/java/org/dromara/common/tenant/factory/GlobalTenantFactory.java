package org.dromara.common.tenant.factory;

import com.mybatisflex.core.tenant.TenantFactory;
import lombok.AllArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.common.tenant.properties.TenantProperties;

import java.util.Set;

/**
 * 全局多租户配置
 *
 * @author hexm
 * @date 2024/03/29 17:26
 */
@AllArgsConstructor
public class GlobalTenantFactory implements TenantFactory {

    private final TenantProperties tenantProperties;

    /**
     * @deprecated 使用 {@link #getTenantIds(String)} 代替。
     */
    @Override
    public Object[] getTenantIds() {
        return new Object[0];
    }

    @Override
    public Object[] getTenantIds(String tableName) {
        if (TenantHelper.isIgnoreDb()) {
            return null;
        }
        if (ignoreTable(tableName)) {
            return null;
        }
        String tenantId = TenantHelper.getTenantId();
        if (StringUtils.isBlank(tenantId)) {
            throw new ServiceException("未能识别到有效tenantId");
        }
        return new Object[]{tenantId};
    }

    public boolean ignoreTable(String tableName) {
        // 不需要过滤租户的表
        Set<String> excludes = tenantProperties.getExcludes();
        // 非业务表
        excludes.add("gen_table");
        excludes.add("gen_table_column");
        return excludes.contains(tableName);
    }
}
