package org.dromara.amqp.msg;

import cn.hutool.core.util.IdUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息基础类，默认分配一个消息id
 *
 * @author hexm
 * @date 2023/5/9 10:50
 */
@Data
public class BaseMqMessage implements Serializable {

    private String msgId;

    public BaseMqMessage() {
        this.msgId = IdUtil.getSnowflakeNextIdStr();
    }
}
