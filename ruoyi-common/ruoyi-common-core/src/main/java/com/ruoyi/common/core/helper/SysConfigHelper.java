package com.ruoyi.common.core.helper;

import com.ruoyi.common.core.service.ConfigService;
import com.ruoyi.common.core.utils.spring.SpringUtils;

/**
 * 系统参数查询帮助类
 *
 * @author hexm
 * @date 2023/02/22 09:20
 */
public class SysConfigHelper {

    private static final ConfigService configService = SpringUtils.getBean(ConfigService.class);

    /**
     * 主框架页-默认皮肤样式名称
     *
     * @return
     */
    public static String getSysIndexSkinName() {
        return configService.getConfigValue("sys.index.skinName");
    }

    /**
     * 用户管理-账号初始密码
     *
     * @return
     */
    public static String getSysUserInitPassword() {
        return configService.getConfigValue("sys.user.initPassword");
    }

    /**
     * 主框架页-侧边栏主题
     *
     * @return
     */
    public static String getSysIndexSideTheme() {
        return configService.getConfigValue("sys.index.sideTheme");
    }

    /**
     * 账号自助-验证码开关
     * 请改为使用application.yml文件配置
     *
     * @return
     */
    @Deprecated
    public static Boolean getSysAccountCaptchaEnabled() {
        return Boolean.TRUE.toString().equals(configService.getConfigValue("sys.account.captchaEnabled"));
    }

    /**
     * 账号自助-是否开启用户注册功能
     *
     * @return
     */
    public static Boolean getSysAccountRegisterUser() {
        return Boolean.TRUE.toString().equals(configService.getConfigValue("sys.account.registerUser"));
    }

    /**
     * OSS预览列表资源开关
     *
     * @return
     */
    public static Boolean getSysOssPreviewListResource() {
        return Boolean.TRUE.toString().equals(configService.getConfigValue("sys.oss.previewListResource"));
    }

    /**
     * 邮箱配置
     *
     * @return
     */
    public static String getSysMail() {
        return configService.getConfigValue("sys.mail");
    }

    /**
     * 短信配置
     *
     * @return
     */
    public static String getSysSms() {
        return configService.getConfigValue("sys.sms");
    }

    /**
     * 短信验证码模板id
     *
     * @return
     */
    public static String getSysSmsCaptchaTemplateId() {
        return configService.getConfigValue("sys.sms.captchaTemplateId");
    }
}
