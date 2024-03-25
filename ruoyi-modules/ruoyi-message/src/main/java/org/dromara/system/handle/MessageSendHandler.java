package org.dromara.system.handle;

import org.dromara.common.core.enums.MessageTypeEnum;
import org.dromara.system.domain.SysMessageConfig;
import org.dromara.system.domain.SysMessageTemplate;

import java.util.Collection;
import java.util.Map;

/**
 * 消息发送处理
 *
 * @author hexm
 * @date 2023/07/24 10:39
 */
public interface MessageSendHandler {

    /**
     * 获取消息类型
     */
    MessageTypeEnum getMessageType();

    /**
     * 发送消息
     *
     * @param account  账号
     * @param message  消息变量
     * @param template 模板对象
     * @param config   消息配置
     */
    void send(Collection<String> account, Map<String, Object> message, SysMessageTemplate template, SysMessageConfig config);
}
