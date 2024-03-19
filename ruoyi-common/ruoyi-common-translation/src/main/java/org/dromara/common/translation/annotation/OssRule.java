package org.dromara.common.translation.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.dromara.common.translation.core.handler.OssRuleHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * OSS规则字段
 * 在Jackson框架中转译字段内容值为指定的规则内容
 *
 * @author hexm
 * @date 2024/03/18 14:19
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = OssRuleHandler.class)
public @interface OssRule {

    /**
     * 使用指定的规则
     */
    String[] value() default {};

    /**
     * 指定规则时，是否使用默认规则。
     * <p>{@link OssRule#value}不存在时，即使是false也将使用默认规则
     */
    boolean useDefault() default false;

    /**
     * 字段与规则的连接符
     */
    String join() default "_";

    /**
     * 映射字段 (如果不为空则取此字段的值)
     */
    String mapper() default "";

    /**
     * 包装方式
     */
    PackingMethod packingMethod() default PackingMethod.UNWRAPPED;

    /**
     * 包装名称。
     * 包装方式为{@link PackingMethod#WRAPPED}时，值将包装在该字段中
     * 字段包装规则：fieldName + wrapName()
     */
    String wrapName() default "Wrap";

    /**
     * 包装方式
     */
    enum PackingMethod {
        /** 包装。将url包装到同一个字段中 */
        WRAPPED,
        /** 不包装。直接在列表中展示 */
        UNWRAPPED
    }
}
