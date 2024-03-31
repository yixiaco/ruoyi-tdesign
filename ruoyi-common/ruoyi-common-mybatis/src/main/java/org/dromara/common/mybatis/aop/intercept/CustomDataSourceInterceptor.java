package org.dromara.common.mybatis.aop.intercept;

import com.mybatisflex.annotation.UseDataSource;
import com.mybatisflex.core.datasource.DataSourceKey;
import com.mybatisflex.core.util.StringUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.dromara.common.core.utils.spring.SpringExpressionUtil;
import org.intellij.lang.annotations.Language;
import org.springframework.core.MethodClassKey;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多数据源切换拦截器，用来对注解支持SpEL表达式解析。
 *
 * @author hexm
 * @date 2024/3/31
 */
public class CustomDataSourceInterceptor implements MethodInterceptor {

    /**
     * 缓存方法对应的数据源。
     */
    protected final Map<MethodClassKey, String> dsCache = new ConcurrentHashMap<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String dsKey = DataSourceKey.getByManual();
        if (StringUtil.isNotBlank(dsKey)) {
            return invocation.proceed();
        }

        dsKey = findDataSourceKey(invocation);
        if (StringUtil.isBlank(dsKey)) {
            return invocation.proceed();
        }

        //方法嵌套时，挂起的 key
        String suspendKey = DataSourceKey.getByAnnotation();

        try {
            DataSourceKey.useWithAnnotation(dsKey);
            return invocation.proceed();
        } finally {
            //恢复挂起的 key
            if (suspendKey != null) {
                DataSourceKey.useWithAnnotation(suspendKey);
            } else {
                DataSourceKey.clear();
            }
        }
    }

    protected String findDataSourceKey(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Class<?> targetClass = invocation.getThis().getClass();
        MethodClassKey cacheKey = new MethodClassKey(method, targetClass);
        String dsKey = this.dsCache.get(cacheKey);
        if (dsKey == null) {
            dsKey = determineDataSourceKey(invocation);
        }
        return dsKey;
    }

    protected String determineDataSourceKey(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Class<?> targetClass = invocation.getThis().getClass();

        // 方法上定义有 UseDataSource 注解
        UseDataSource annotation = getDataSourceAnnotation(method, targetClass);

        if (annotation != null) {
            @Language("SpEL") String value = annotation.value();
            if (value.contains("#")) {
                return SpringExpressionUtil.parseClassTemplateExpression(value, invocation.getMethod(), invocation.getArguments());
            } else {
                dsCache.put(new MethodClassKey(method, targetClass), value);
                return value;
            }
        }

        return "";
    }

    protected UseDataSource getDataSourceAnnotation(Method method, Class<?> targetClass) {

        // 方法上定义有 UseDataSource 注解
        UseDataSource annotation = method.getAnnotation(UseDataSource.class);
        if (annotation != null) {
            return annotation;
        }

        // 类上定义有 UseDataSource 注解
        annotation = targetClass.getAnnotation(UseDataSource.class);
        if (annotation != null) {
            return annotation;
        }

        // 接口上定义有 UseDataSource 注解
        Class<?>[] interfaces = targetClass.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            annotation = anInterface.getAnnotation(UseDataSource.class);
            if (annotation != null) {
                return annotation;
            }
        }

        return null;
    }

}
