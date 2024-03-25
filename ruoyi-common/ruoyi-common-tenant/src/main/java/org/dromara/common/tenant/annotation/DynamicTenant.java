package org.dromara.common.tenant.annotation;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态租户注解
 * 需要注意的是，多个注解切面优先级不能高于租户切面(-1)，否则无法对该注解生效
 * <p>在Spring管理的Bean中使用</p>
 * <p>
 * example:
 * <pre>{@code
 *      @DynamicTenant(value  = "#msg.tenantId")
 *      public <T extends TenantMQMessage> void example(T msg) {
 *          ...
 *      }
 * }</pre>
 *
 * @author hexm
 * @date 2023/07/22 11:59
 * @see org.dromara.common.tenant.msg.TenantMQMessage
 * @see org.dromara.common.tenant.aspect.TenantAspect#handleDynamic
 * @since 1.0.7
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicTenant {

    /**
     * 使用的租户id。注意，这是一个SpringEL表达式
     */
    @Language("SpEL")
    String value();

    /**
     * 当读取不到租户时将抛出异常，设置为false将阻止异常抛出
     */
    boolean required() default true;

    /**
     * 是否可重入。当方法嵌套时，可重入可以使租户不会提前被释放
     */
    boolean reentrant() default true;
}
