package org.dromara.common.core.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.dromara.common.core.validation.validator.AnyStringConstraintValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对属性中的值，判定是否在数组中的任意一个
 * 对于一个空字符串认为是有效的
 *
 * @author hexm
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AnyStringConstraintValidator.class)
public @interface AnyString {

    String[] value() default {};

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
