package org.dromara.common.mybatis.annotation;

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
@Target(ElementType.FIELD)
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

    enum FillType {
        /**
         * 插入时填充字段
         */
        INSERT,
        /**
         * 更新时填充字段
         */
        UPDATE,
        /**
         * 插入和更新时填充字段
         */
        INSERT_UPDATE
    }

    enum DateType {
        /**
         * 时间类型，支持类型：Date、Long、LocalDateTime、LocalDate、LocalTime、Instant、Timestamp
         */
        DATE,
        /**
         * 当前用户id，支持类型: String、Long
         */
        USER_ID,
        /**
         * 用户名称，支持类型: String
         */
        USERNAME,
        /**
         * 部门id，支持类型: String、Long
         */
        DEPT_ID,
        /**
         * 当前请求IP，支持类型: String
         */
        IP,
        /**
         * ip地址，支持类型: String
         */
        IP_ADDRESS;
    }
}
