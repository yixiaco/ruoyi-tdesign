package org.dromara.common.mybatis.aop;

import com.mybatisflex.spring.datasource.DataSourceAdvice;
import org.aopalliance.aop.Advice;
import org.dromara.common.mybatis.aop.intercept.CustomDataSourceInterceptor;

/**
 * 自定义多数据源切面，支持SpEL表达式注入。
 *
 * @author hexm
 * @date 2024/3/31
 */
public class CustomDataSourceAdvice extends DataSourceAdvice {

    // 覆盖父类的属性
    private final Advice advice;

    public CustomDataSourceAdvice() {
        this(new CustomDataSourceInterceptor());
    }

    public CustomDataSourceAdvice(Advice advice) {
        this.advice = advice;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

}
