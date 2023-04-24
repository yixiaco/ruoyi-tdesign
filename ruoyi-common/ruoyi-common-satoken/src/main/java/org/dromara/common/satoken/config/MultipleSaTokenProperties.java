package org.dromara.common.satoken.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 多账号体系配置
 *
 * @author hexm
 * @date 2023/04/23 17:13
 */
@Data
@ConfigurationProperties(prefix = "sa-token")
public class MultipleSaTokenProperties {

    /**
     * 多账号体系配置,登录类型对应的配置
     */
    private Map<String, MultipleSaTokenConfig> multiple;
}
