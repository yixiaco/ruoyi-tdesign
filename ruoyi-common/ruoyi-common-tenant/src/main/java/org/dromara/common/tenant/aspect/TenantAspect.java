package org.dromara.common.tenant.aspect;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.spring.SpringExpressionUtil;
import org.dromara.common.tenant.annotation.DynamicTenant;
import org.dromara.common.tenant.annotation.IgnoreTenant;
import org.dromara.common.tenant.helper.TenantHelper;
import org.springframework.core.annotation.Order;

/**
 * 租户切面
 *
 * @author hexm
 * @date 2023/04/20 10:04
 */
@Aspect
@Order(-1)
public class TenantAspect {

    /**
     * 处理忽略租户
     *
     * @param point
     * @param ignoreTenant
     * @return
     * @throws Throwable
     */
    @Around("@within(ignoreTenant) || @annotation(ignoreTenant)")
    public Object handleIgnore(ProceedingJoinPoint point, IgnoreTenant ignoreTenant) throws Throwable {
        if (ignoreTenant.db()) {
            TenantHelper.enableIgnore();
        }
        if (ignoreTenant.cache()) {
            TenantHelper.enableIgnoreCache();
        }
        try {
            return point.proceed();
        } finally {
            if (ignoreTenant.db()) {
                TenantHelper.disableIgnore();
            }
            if (ignoreTenant.cache()) {
                TenantHelper.disableIgnoreCache();
            }
        }
    }

    /**
     * 处理动态租户
     *
     * @param point
     * @param dynamicTenant
     * @return
     * @throws Throwable
     */
    @Around("@within(dynamicTenant) || @annotation(dynamicTenant)")
    public Object handleDynamic(ProceedingJoinPoint point, DynamicTenant dynamicTenant) throws Throwable {
        if (StrUtil.isBlank(dynamicTenant.value())) {
            throw new ServiceException("动态租户不存在！");
        }
        String tenantId = SpringExpressionUtil.parseAspectExpression(dynamicTenant.value(), point);
        if (StrUtil.isBlank(tenantId)) {
            throw new ServiceException("动态租户不存在！");
        }
        DynamicTenant.DYNAMIC_TENANT_AOP.set(tenantId);
        try {
            return point.proceed();
        } finally {
            DynamicTenant.DYNAMIC_TENANT_AOP.remove();
        }
    }
}
