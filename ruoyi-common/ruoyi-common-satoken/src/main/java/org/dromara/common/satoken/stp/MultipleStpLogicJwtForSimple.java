package org.dromara.common.satoken.stp;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;

/**
 * Sa-Token 整合 jwt 多配置 -- Simple 简单模式
 *
 * @author hexm
 * @date 2023/04/24 12:06
 */
public class MultipleStpLogicJwtForSimple extends StpLogicJwtForSimple implements MultipleStpLogicInterface {

    private final SaTokenConfig config;

    /**
     * 初始化StpLogic, 并指定账号类型
     *
     * @param loginType 账号体系标识
     * @param config    配置
     */
    public MultipleStpLogicJwtForSimple(String loginType, SaTokenConfig config) {
        super(loginType);
        this.config = config;
    }

    @Override
    public SaTokenConfig getConfig() {
        return config;
    }
}
