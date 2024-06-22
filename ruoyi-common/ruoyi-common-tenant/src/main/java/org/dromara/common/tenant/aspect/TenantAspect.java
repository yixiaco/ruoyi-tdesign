package org.dromara.common.tenant.aspect;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.spring.SpringExpressionCreated;
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
     * @param point        织入点
     * @param ignoreTenant 忽略租户注解
     * @return
     * @throws Throwable
     */
    @Around("@annotation(ignoreTenant)")
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
     * @param point         织入点
     * @param dynamicTenant 动态租户注解
     * @return
     * @throws Throwable
     */
    @Around("@annotation(dynamicTenant)")
    public Object handleDynamic(ProceedingJoinPoint point, DynamicTenant dynamicTenant) throws Throwable {
        if (StrUtil.isBlank(dynamicTenant.value())) {
            throw new ServiceException("动态租户未配置值！");
        }
        String tenantId;
        if (dynamicTenant.value().contains(SpringExpressionCreated.PARSER_CONTEXT.getExpressionPrefix())) {
            tenantId = SpringExpressionCreated.createStandardTemplate(point).getValueString(dynamicTenant.value());
        } else {
            tenantId = SpringExpressionCreated.createStandard(point).getValueString(dynamicTenant.value());
        }
        boolean blank = StrUtil.isBlank(tenantId);
        if (blank && dynamicTenant.required()) {
            throw new ServiceException("动态租户不存在！");
        }
        if (!blank) {
            TenantHelper.setDynamicTenant(tenantId);
        }
        try {
            return point.proceed();
        } finally {
            if (!blank) {
                TenantHelper.removeDynamicTenant(!dynamicTenant.reentrant());
            }
        }
    }
}
