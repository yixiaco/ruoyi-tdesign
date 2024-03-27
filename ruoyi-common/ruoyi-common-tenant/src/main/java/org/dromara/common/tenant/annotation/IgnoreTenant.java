package org.dromara.common.tenant.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略租户注解
 * 需要注意的是，多个注解切面优先级不能高于租户切面(-1)，否则无法对该注解生效
 *
 * @author hexm
 * @date 2023/04/20 10:06
 * @see org.dromara.common.tenant.aspect.TenantAspect#handleIgnore
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreTenant {

    /**
     * 对事务生效
     */
    boolean db() default true;

    /**
     * 对缓存生效，效果与使用全局缓存一致
     * 全局缓存使用{@link org.dromara.common.core.constant.GlobalConstants#GLOBAL_REDIS_KEY}或设置该参数为true
     */
    boolean cache() default false;
}
