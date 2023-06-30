package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 消息配置查询对象 sys_message_config
 *
 * @author hexm
 * @date 2023-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMessageConfigQuery extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 支持平台标识
     */
    private String supplierType;

    /**
     * 状态（1正常 0停用）
     */
    private Integer status;

}
