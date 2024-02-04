package org.dromara.common.core.constant;

/**
 * 用户常量信息
 *
 * @author ruoyi
 */
public interface UserConstants {

    /**
     * 平台内系统用户的唯一标志
     */
    String SYS_USER = "SYS_USER";

    /**
     * Layout组件标识
     */
    String LAYOUT = "Layout";

    /**
     * ParentView组件标识
     */
    String PARENT_VIEW = "ParentView";

    /**
     * InnerLink组件标识
     */
    String INNER_LINK = "InnerLink";

    /**
     * 用户名长度限制
     */
    int USERNAME_MIN_LENGTH = 2;
    int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    int PASSWORD_MIN_LENGTH = 5;
    int PASSWORD_MAX_LENGTH = 20;

    /**
     * 超级管理员ID
     */
    Long SUPER_ADMIN_ID = 1L;

}
