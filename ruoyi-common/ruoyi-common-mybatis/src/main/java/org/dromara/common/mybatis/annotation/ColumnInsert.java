package org.dromara.common.mybatis.annotation;

import org.dromara.common.mybatis.enums.DateType;
import org.dromara.common.mybatis.enums.FillType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mybatis-flex数据插入时填充
 * 在需要填充的字段上注释该注解，对象需要实现{@link java.io.Serializable}
 *
 * @author hexm
 * @date 2024/03/29 15:37
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ColumnFill(fillType = FillType.INSERT, dateType = DateType.None)
public @interface ColumnInsert {

    /**
     * 数据填充类型
     */
    @AliasFor(annotation = ColumnFill.class, attribute = "dateType")
    DateType dateType();
}
