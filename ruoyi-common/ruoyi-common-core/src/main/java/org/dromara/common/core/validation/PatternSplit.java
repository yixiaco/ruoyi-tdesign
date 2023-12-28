package org.dromara.common.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.dromara.common.core.validation.validator.PatternSplitValidator;
import org.intellij.lang.annotations.Language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字符串分割匹配
 *
 * @author hexm
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PatternSplitValidator.class)
public @interface PatternSplit {

    /**
     * 一个有效的正则表达式，匹配分割后的字符串
     */
    @Language("RegExp")
    String regexp();

    /**
     * 分割符正则
     */
    @Language("RegExp")
    String split() default ",";

    /**
     * 错误消息
     *
     * @return
     */
    String message() default "格式错误";

    /**
     * 是否是必须的，否则返回错误
     *
     * @return
     */
    boolean required() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
