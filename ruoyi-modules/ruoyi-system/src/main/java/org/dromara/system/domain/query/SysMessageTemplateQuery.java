package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 消息模板查询对象 sys_message_template
 *
 * @author hexm
 * @date 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMessageTemplateQuery extends BaseEntity {

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 消息key
     */
    private String messageKey;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 模板类型
     */
    private String templateMode;

    /**
     * 状态（1正常 0停用）
     */
    private Integer status;

}
