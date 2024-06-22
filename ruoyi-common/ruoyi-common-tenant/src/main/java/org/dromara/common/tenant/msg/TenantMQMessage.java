package org.dromara.common.tenant.msg;

import cn.hutool.core.util.IdUtil;
import lombok.Data;
import org.dromara.common.tenant.helper.TenantHelper;

import java.io.Serializable;

/**
 * 租户消息基础类，默认分配一个消息id、租户id
 *
 * @author hexm
 * @date 2023/07/22 12:14
 * @see org.dromara.common.tenant.annotation.DynamicTenant
 * @since 1.0.7
 */
@Data
public class TenantMQMessage implements Serializable {

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 租户id
     */
    private String tenantId;

    public TenantMQMessage() {
        this.msgId = IdUtil.getSnowflakeNextIdStr();
        this.tenantId = TenantHelper.getTenantId();
    }
}
