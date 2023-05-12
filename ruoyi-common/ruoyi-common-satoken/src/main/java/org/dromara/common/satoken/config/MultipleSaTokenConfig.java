package org.dromara.common.satoken.config;

import cn.dev33.satoken.config.SaTokenConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.satoken.stp.MultipleStpLogic;
import org.dromara.common.satoken.stp.MultipleStpLogicInterface;

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
     * 权限认证实现类
     */
    private Class<? extends MultipleStpLogicInterface> logicClass = MultipleStpLogic.class;

    /**
     * 权限匹配路径
     */
    private List<String> match;
}
