package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 客户端管理查询对象 sys_client
 *
 * @author yixiacoco
 * @date 2023-07-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysClientQuery extends BaseEntity {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端key
     */
    private String clientKey;

    /**
     * 状态（1正常 0停用）
     */
    private String status;

}
