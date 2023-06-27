package org.dromara.common.satoken.annotation;

import cn.dev33.satoken.annotation.SaCheckSafe;
import cn.dev33.satoken.util.SaTokenConsts;
import org.dromara.common.satoken.utils.MultipleStpUtil;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 预设登录认证(User版)
 * 二级认证校验：必须二级认证之后才能进入该方法
 *
 * <p> 可标注在函数、类上（效果等同于标注在此类的所有方法上）
 *
 * @author kong
 */
@SaCheckSafe(type = MultipleStpUtil.USER_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SaUserCheckSafe {

    /**
     * 要校验的服务
     *
     * @return /
     */
    @AliasFor(annotation = SaCheckSafe.class, attribute = "value")
    String value() default SaTokenConsts.DEFAULT_SAFE_AUTH_SERVICE;

}
