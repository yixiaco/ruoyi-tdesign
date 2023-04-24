package org.dromara.common.satoken.config;

import cn.dev33.satoken.config.SaTokenConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 多账号体系配置项
 *
 * @author hexm
 * @date 2023/04/24 17:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MultipleSaTokenConfig extends SaTokenConfig {

    /**
     * 权限匹配路径
     */
    private List<String> match;
}
