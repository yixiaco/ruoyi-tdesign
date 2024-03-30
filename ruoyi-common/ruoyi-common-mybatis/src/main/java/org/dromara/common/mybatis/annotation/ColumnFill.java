package org.dromara.common.mybatis.annotation;

import org.dromara.common.mybatis.enums.DateType;
import org.dromara.common.mybatis.enums.FillType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mybatis-flex数据填充
 * 在需要填充的字段上注释该注解，对象需要实现{@link java.io.Serializable}
 *
 * @author hexm
 * @date 2024/03/29 15:37
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ColumnFill {
    /**
     * 字段自动填充策略。如果数据已经存在，则不覆盖填充
     */
    FillType fillType();

    /**
     * 数据填充类型
     */
    DateType dateType();
}
