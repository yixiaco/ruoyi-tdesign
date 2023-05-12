package org.dromara.common.satoken.stp;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpLogic;
import org.dromara.common.satoken.config.MultipleSaTokenConfig;
import org.dromara.common.satoken.config.MultipleSaTokenProperties;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 返回动态登录操作对象
 *
 * @author hexm
 * @date 2023/04/24 11:25
 */
public class DynamicStpLogic {

    public static MultipleSaTokenProperties config;
    private static final Map<String, StpLogic> DYNAMIC = new HashMap<>();

    /**
     * 设置配置
     *
     * @param config 配置对象
     */
    public static void setConfig(MultipleSaTokenProperties config) {
        DynamicStpLogic.config = config;
    }

    /**
     * 获取登录类型操作对象
     *
     * @param type 登录类型
     * @return
     */
    public static StpLogic getDynamicStpLogic(String type) {
        if (getNames().contains(type)) {
            StpLogic stpLogic = DYNAMIC.get(type);
            if (stpLogic == null) {
                synchronized (DYNAMIC) {
                    stpLogic = DYNAMIC.get(type);
                    if (stpLogic == null) {
                        MultipleSaTokenConfig tokenConfig = config.getMultiple().get(type);
                        try {
                            stpLogic = (StpLogic) tokenConfig.getLogicClass()
                                .getDeclaredConstructor(String.class, SaTokenConfig.class)
                                .newInstance(type, tokenConfig);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                 NoSuchMethodException e) {
                            throw new SaTokenException("Initialize MultipleStpLogic【" + type + "】 implementation class exceptions：" + e.getMessage());
                        }
                        DYNAMIC.put(type, stpLogic);
                    }
                }
            }
            return stpLogic;
        }
        throw new SaTokenException("dynamic login type not found: " + type);
    }

    /**
     * 返回所有登录类型
     *
     * @return
     */
    public static Set<String> getNames() {
        return config.getMultiple().keySet();
    }
}
