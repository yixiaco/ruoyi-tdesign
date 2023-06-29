package org.dromara.system.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 消息发送记录对象 sys_message_log
 *
 * @author hexm
 * @date 2023-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_message_log")
public class SysMessageLog extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息发送记录id
     */
    @TableId(value = "message_log_id")
    private Long messageLogId;

    /**
     * 消息key
     */
    private String messageKey;

    /**
     * 模板名称
     */
    private String messageTemplateName;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 模板类型
     */
    private String templateMode;

    /**
     * 发送账号
     */
    private String account;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 消息配置标题
     */
    private String messageConfigTitle;

    /**
     * 平台标识
     */
    private String supplierType;

    /**
     * 是否成功
     */
    private Integer isSuccess;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 回执消息id
     */
    private String bizId;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 记录时间
     */
    private Date logTime;

}
