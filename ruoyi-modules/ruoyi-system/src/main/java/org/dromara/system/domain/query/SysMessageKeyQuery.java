package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 消息常量查询对象 sys_message_key
 *
 * @author hexm
 * @date 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMessageKeyQuery extends BaseEntity {

    /**
     * 消息名称
     */
    private String name;

    /**
     * 消息key
     */
    private String messageKey;

}
