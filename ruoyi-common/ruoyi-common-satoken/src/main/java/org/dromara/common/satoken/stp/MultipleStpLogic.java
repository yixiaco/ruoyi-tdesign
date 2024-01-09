package org.dromara.common.satoken.stp;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.stp.StpLogic;

/**
 * Sa-Token 多配置权限认证，逻辑实现类
 *
 * @author hexm
 * @date 2023/04/24 14:21
 */
public class MultipleStpLogic extends StpLogic implements MultipleStpLogicInterface {

    private final SaTokenConfig config;

    /**
     * 初始化StpLogic, 并指定账号类型
     *
     * @param loginType 账号体系标识
     * @param config    配置
     */
    public MultipleStpLogic(String loginType, SaTokenConfig config) {
        super(loginType);
        this.config = config;
    }

    @Override
    public SaTokenConfig getConfig() {
        return config;
    }
}
