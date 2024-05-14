package org.dromara.common.core.constant;

/**
 * 全局的key常量 (业务无关的key)
 *
 * @author Lion Li
 */
public interface GlobalConstants {

    /**
     * 全局 redis key (业务无关的key)
     */
    String GLOBAL_REDIS_KEY = "global:";

    /**
     * 验证码 redis key
     */
    String CAPTCHA_CODE_KEY = GLOBAL_REDIS_KEY + "captcha_codes:";

    /**
     * 防重提交 redis key
     */
    String REPEAT_SUBMIT_KEY = GLOBAL_REDIS_KEY + "repeat_submit:";

    /**
     * 限流 redis key
     */
    String RATE_LIMIT_KEY = GLOBAL_REDIS_KEY + "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    String PWD_ERR_CNT_KEY = GLOBAL_REDIS_KEY + "pwd_err_cnt:";

    /**
     * 租户应用key redis map key
     */
    String TENANT_APP_KEY = GLOBAL_REDIS_KEY + "tenant_app_key";

    /**
     * 三方认证 redis key
     */
    String SOCIAL_AUTH_CODE_KEY = GLOBAL_REDIS_KEY + "social_auth_codes:";

    /**
     * 在线租户id redis key
     */
    String ONLINE_TOKEN_TENANT_ID_KEY = GLOBAL_REDIS_KEY + "online_token_tenant_id:";

    /**
     * 获取全局key
     *
     * @param key key
     * @return
     */
    static String getGlobalKey(String key) {
        return GLOBAL_REDIS_KEY + key;
    }

    /**
     * 获取全局key
     *
     * @param isGlobal 是否获取全局key
     * @param key      key
     * @return 如果isGlobal为true，则返回全局key，否则直接返回key
     */
    static String getGlobalKey(boolean isGlobal, String key) {
        return isGlobal ? getGlobalKey(key) : key;
    }
}
