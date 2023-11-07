package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 消息发送记录查询对象 sys_message_log
 *
 * @author hexm
 * @date 2023-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMessageLogQuery extends BaseEntity {

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
     * 模板ID
     */
    private String templateId;

    /**
     * 平台标识
     */
    private String supplierType;

    /**
     * 是否成功
     */
    private Integer isSuccess;

}
