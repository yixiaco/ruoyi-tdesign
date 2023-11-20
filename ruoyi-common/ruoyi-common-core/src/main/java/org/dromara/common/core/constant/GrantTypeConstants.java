package org.dromara.common.core.constant;

/**
 * 授权类型常量
 *
 * @author hexm
 * @date 2023/11/20 11:38
 */
public interface GrantTypeConstants {
    /** 邮箱认证 */
    String EMAIL = "email";
    /** 密码认证 */
    String PASSWORD = "password";
    /** 第三方授权认证 */
    String SOCIAL = "social";
    /** 小程序认证 */
    String XCX = "xcx";
    /** 短信认证 */
    String SMS = "sms";
}
