package org.dromara.system.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import org.dromara.common.mybatis.annotation.ColumnInsert;
import org.dromara.common.mybatis.enums.DateType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息发送记录对象 sys_message_log
 *
 * @author hexm
 * @date 2023-06-29
 */
@Data
@Table("sys_message_log")
public class SysMessageLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息发送记录id
     */
    @Id
    private Long messageLogId;

    /**
     * 消息模板id
     */
    private Long messageTemplateId;

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
     * 标题
     */
    private String title;

    /**
     * 模板ID
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
     * 返回主体消息
     */
    private String responseBody;

    /**
     * 记录时间
     */
    @ColumnInsert(dateType = DateType.DATE)
    private Date logTime;

    /**
     * 消耗时间
     */
    private Long costTime;

}
