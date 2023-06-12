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
    String CAPTCHA_CODE_KEY = getGlobalKey("captcha_codes:");

    /**
     * 防重提交 redis key
     */
    String REPEAT_SUBMIT_KEY = getGlobalKey("repeat_submit:");

    /**
     * 限流 redis key
     */
    String RATE_LIMIT_KEY = getGlobalKey("rate_limit:");

    /**
     * 登录账户密码错误次数 redis key
     */
    String PWD_ERR_CNT_KEY = getGlobalKey("pwd_err_cnt:");

    /**
     * 应用key redis key
     */
    String APP_KEY = getGlobalKey("app_key");

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
