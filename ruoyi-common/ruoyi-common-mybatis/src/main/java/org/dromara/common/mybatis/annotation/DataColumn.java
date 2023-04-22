package org.dromara.common.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限
 * <p>
 * 一个注解只能对应一个模板
 *
 * @author Lion Li
 * @version 3.5.0
 * @see org.dromara.common.mybatis.annotation.DataPermission
 * @see org.dromara.common.mybatis.enums.DataScopeType
 * @see org.dromara.common.mybatis.handler.PlusDataPermissionHandler
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataColumn {

    /**
     * 占位符关键字
     */
    String[] key() default "deptName";

    /**
     * 占位符替换值
     */
    String[] value() default "dept_id";

}
