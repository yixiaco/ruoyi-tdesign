package org.dromara.common.log.event;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录日志事件
 *
 * @author Lion Li
 */
@Data
public class LogininforEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 登录状态 0成功 1失败
     */
    private String status;

    /**
     * 提示消息
     */
    private String message;

    /**
     * ip
     */
    private String ip;

    /**
     * 客户端操作系统
     */
    private String os;

    /**
     * 客户端浏览器
     */
    private String browser;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 其他参数
     */
    private Object[] args;

}
