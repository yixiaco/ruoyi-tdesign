package org.dromara.common.mybatis.config;

import com.github.pagehelper.PageInterceptor;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.spring.boot.MultiDataSourceAutoConfiguration;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.mybatisflex.spring.datasource.DataSourceAdvice;
import org.apache.ibatis.plugin.Interceptor;
import org.dromara.common.mybatis.aop.CustomDataSourceAdvice;
import org.dromara.common.mybatis.listener.GlobalMybatisFlexListener;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import java.io.Serializable;

/**
 * mybatis-flex配置类
 *
 * @author hexm
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(MultiDataSourceAutoConfiguration.class)
public class MyBatisFlexConfiguration implements MyBatisFlexCustomizer {

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        //我们可以在这里进行一些列的初始化配置
        globalConfig.registerInsertListener(new GlobalMybatisFlexListener(), Serializable.class);
        globalConfig.registerUpdateListener(new GlobalMybatisFlexListener(), Serializable.class);
        // TODO: 自定义更新策略
//        // 使用内置规则自动忽略 null 和 空字符串
//        QueryColumnBehavior.setIgnoreFunction(QueryColumnBehavior.IGNORE_EMPTY);
//        // 使用内置规则自动忽略 null 和 空白字符串
//        QueryColumnBehavior.setIgnoreFunction(QueryColumnBehavior.IGNORE_BLANK);
//        // 其他自定义规则
//        QueryColumnBehavior.setIgnoreFunction(o -> {...});
    }

    /**
     * 分页插件
     */
    @Bean
    public Interceptor pageInterceptor() {
        return new PageInterceptor();
    }

    /**
     * {@link com.mybatisflex.annotation.UseDataSource} 注解切换数据源切面
     * <p>
     * 使{@link com.mybatisflex.annotation.UseDataSource}支持SpEL表达式的注入。
     * <p>
     * 需要注意的是，若表达式中不包含'#'符号，则认为是一个普通字符串
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DataSourceAdvice dataSourceAdvice() {
        return new CustomDataSourceAdvice();
    }
}
