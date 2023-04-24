package org.dromara.common.satoken.stp;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;

/**
 * Sa-Token 整合 jwt 多配置 -- Simple 简单模式
 * @author hexm
 * @date 2023/04/24 12:06
 */
public class MultipleStpLogicJwtForSimple extends StpLogicJwtForSimple {

    private SaTokenConfig config;

    public MultipleStpLogicJwtForSimple(String type, SaTokenConfig config) {
        super(type);
        this.config = config;
    }

    @Override
    public SaTokenConfig getConfig() {
        return config;
    }
}
