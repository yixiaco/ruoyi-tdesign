package org.dromara.common.satoken.utils;

import cn.dev33.satoken.stp.StpLogic;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.satoken.stp.DynamicStpLogic;

/**
 * 多账号体系登录操作工具
 *
 * @author hexm
 * @date 2023/04/24 11:40
 */
@Slf4j
public class MultipleStpUtil {

    public static final String LOGIN_USER_KEY = "loginUser";
    public static final String TENANT_KEY = "tenantId";
    public static final String USER_KEY = "userId";

    private MultipleStpUtil() {
    }

    /**
     * 系统登录类型
     */
    public static final String SYSTEM_TYPE = "login";
    /**
     * 用户登录类型
     */
    public static final String USER_TYPE = "user";

    /**
     * 系统登录类型操作对象
     */
    public static StpLogic SYSTEM;
    /**
     * 用户登录类型操作对象
     */
    public static StpLogic USER;

    static {
        try {
            SYSTEM = DynamicStpLogic.getDynamicStpLogic(SYSTEM_TYPE);
            USER = DynamicStpLogic.getDynamicStpLogic(USER_TYPE);
        } catch (Exception e) {
            log.error("MultipleStpLogic 初始化异常", e);
        }
    }


}
