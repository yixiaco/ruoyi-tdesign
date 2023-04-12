package org.dromara.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 用户配置
 *
 * @author hexm
 * @date 2023/01/14 14:29
 */
@Data
@Component
@ConfigurationProperties(prefix = "user.password")
public class UserLoginConfig {
    /** 密码最大错误次数 */
    private Integer maxRetryCount;
    /** 密码锁定时间 */
    private Duration lockTime = Duration.ofMinutes(10);
}
