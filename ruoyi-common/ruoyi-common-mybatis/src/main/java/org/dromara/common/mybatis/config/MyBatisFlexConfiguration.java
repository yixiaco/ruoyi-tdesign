package org.dromara.common.mybatis.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.dromara.common.mybatis.listener.GlobalMybatisFlexListener;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * mybatis-flex配置类
 *
 * @author hexm
 */
@Configuration
public class MyBatisFlexConfiguration implements MyBatisFlexCustomizer {

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        //我们可以在这里进行一些列的初始化配置
        globalConfig.registerInsertListener(new GlobalMybatisFlexListener(), Serializable.class);
    }

}
