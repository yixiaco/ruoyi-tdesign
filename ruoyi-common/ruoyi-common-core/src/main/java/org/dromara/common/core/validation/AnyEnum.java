package org.dromara.common.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.dromara.common.core.validation.validator.AnyEnumValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对字符串验证枚举
 *
 * @author hexm
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AnyEnumValidator.class)
public @interface AnyEnum {

    /**
     * 一个有效的枚举类
     *
     * @return
     */
    Class<? extends Enum<?>>[] value() default {};

    /**
     * 比较属性名称,如果获取不到属性，则默认执行name()方法
     */
    String field() default "";

    /**
     * 是否忽略大小写
     *
     * @return
     */
    boolean ignoreCase() default false;

    /**
     * 错误消息
     *
     * @return
     */
    String message() default "不是一个有效的字符";

    /**
     * 是否是必须的，否则返回错误
     *
     * @return
     */
    boolean required() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
