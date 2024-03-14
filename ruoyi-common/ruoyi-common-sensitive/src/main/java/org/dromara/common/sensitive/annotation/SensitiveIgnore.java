package org.dromara.common.sensitive.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略数据脱敏注解
 *
 * @author hexm
 * @date 2024/03/13 15:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@JacksonAnnotation
public @interface SensitiveIgnore {
}
