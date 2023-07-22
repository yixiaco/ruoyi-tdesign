package org.dromara.common.tenant.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.amqp.msg.BaseMqMessage;
import org.dromara.common.tenant.helper.TenantHelper;

/**
 * 租户MQ消息
 *
 * @author hexm
 * @date 2023/07/22 12:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantMQMessage extends BaseMqMessage {

    /**
     * 租户id
     */
    private String tenantId;

    public TenantMQMessage() {
        this.tenantId = TenantHelper.getTenantId();
    }
}
