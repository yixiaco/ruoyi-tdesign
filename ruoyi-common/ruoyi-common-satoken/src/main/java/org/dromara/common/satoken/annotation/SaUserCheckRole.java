package org.dromara.common.satoken.annotation;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import org.dromara.common.satoken.utils.MultipleStpUtil;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 预设登录认证(User版)
 * 角色认证校验：必须具有指定角色标识才能进入该方法
 * <p> 可标注在函数、类上（效果等同于标注在此类的所有方法上）
 *
 * @author hexm
 */
@SaCheckRole(type = MultipleStpUtil.USER_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SaUserCheckRole {

    /**
     * 需要校验的角色标识
     *
     * @return 需要校验的角色标识
     */
    @AliasFor(annotation = SaCheckRole.class, attribute = "value")
    String[] value() default {};

    /**
     * 验证模式：AND | OR，默认AND
     *
     * @return 验证模式
     */
    @AliasFor(annotation = SaCheckRole.class, attribute = "mode")
    SaMode mode() default SaMode.AND;

}
