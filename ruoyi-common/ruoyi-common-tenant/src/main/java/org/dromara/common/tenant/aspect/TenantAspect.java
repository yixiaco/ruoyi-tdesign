package org.dromara.common.tenant.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
}
