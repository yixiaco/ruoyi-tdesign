package org.dromara.common.satoken.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.satoken.core.dao.PlusSaTokenDao;
import org.dromara.common.satoken.core.service.SaPermissionImpl;
import org.dromara.common.satoken.online.DefaultOnlineUserCacheManager;
import org.dromara.common.satoken.online.OnlineUserCacheManager;
import org.dromara.common.satoken.stp.DynamicStpLogic;
import org.dromara.common.satoken.utils.MultipleStpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

/**
 * sa-token 配置
 *
 * @author Lion Li
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MultipleSaTokenProperties.class)
public class SaTokenConfiguration implements WebMvcConfigurer {

    /**
     * 权限接口实现(使用bean注入方便用户替换)
     */
    @Bean
    public StpInterface stpInterface() {
        return new SaPermissionImpl();
    }

    /**
     * 自定义dao层存储
     */
    @Bean
    public SaTokenDao saTokenDao() {
        return new PlusSaTokenDao();
    }

    /**
     * 初始化配置多配置satoken
     *
     * @param config 多配置
     */
    @Autowired
    public void initConfig(MultipleSaTokenProperties config) {
        Map<String, MultipleSaTokenConfig> multiple = config.getMultiple();
        if (multiple.size() != multiple.values().stream().map(SaTokenConfig::getTokenName).distinct().count()) {
            log.warn("saToken初始化配置: 存在相同的token名称，可能会影响到token的读取！！！");
        }
        // 初始化动态配置
        DynamicStpLogic.setConfig(config);
        // 初始化默认配置
        StpUtil.setStpLogic(MultipleStpUtil.SYSTEM);
    }

    /**
     * 注解合并
     */
    @Autowired
    public void rewriteSaStrategy() {
        // 重写Sa-Token的注解处理器，增加注解合并功能
        SaStrategy.me.getAnnotation = AnnotatedElementUtils::getMergedAnnotation;
    }

    /**
     * 在线用户管理
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OnlineUserCacheManager.class)
    public OnlineUserCacheManager defaultOnlineUserCacheManager() {
        return new DefaultOnlineUserCacheManager();
    }
}
