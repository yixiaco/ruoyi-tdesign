package org.dromara.common.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限组
 *
 * @author Lion Li
 * @version 3.5.0
 * @see org.dromara.common.mybatis.annotation.DataColumn
 * @see org.dromara.common.mybatis.enums.DataScopeType
 * @see org.dromara.common.mybatis.handler.PlusDataPermissionHandler
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    DataColumn[] value();

}
