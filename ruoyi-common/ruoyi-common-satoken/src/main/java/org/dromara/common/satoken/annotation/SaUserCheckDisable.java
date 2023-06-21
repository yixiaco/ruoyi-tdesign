package org.dromara.common.satoken.annotation;

import cn.dev33.satoken.annotation.SaCheckDisable;
import cn.dev33.satoken.util.SaTokenConsts;
import org.dromara.common.satoken.utils.MultipleStpUtil;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 预设登录认证(User版)
 * 服务禁用校验：在没有被禁用指定服务的情况下才可以进入方法
 *
 * <p> 可标注在函数、类上（效果等同于标注在此类的所有方法上）
 *
 * @author hexm
 */
@SaCheckDisable(type = MultipleStpUtil.USER_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SaUserCheckDisable {

    /**
     * 服务标识 （要校验是否禁用的服务名称）
     *
     * @return see note
     */
    @AliasFor(annotation = SaCheckDisable.class, attribute = "value")
    String[] value() default {SaTokenConsts.DEFAULT_DISABLE_SERVICE};

    /**
     * 封禁等级（只有 封禁等级 ≥ 此值 才会抛出异常）
     *
     * @return /
     */
    @AliasFor(annotation = SaCheckDisable.class, attribute = "level")
    int level() default SaTokenConsts.DEFAULT_DISABLE_LEVEL;

}
