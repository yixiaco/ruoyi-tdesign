package org.dromara.system.service;

import org.dromara.common.core.enums.MessageTypeEnum;

import java.util.Collection;
import java.util.Map;

/**
 * 消息发送Service接口
 *
 * @author hexm
 * @date 2023/06/29 16:14
 */
public interface ISysMessageSendService {

    /**
     * 发送消息
     *
     * @param messageTemplateId 消息模板id
     * @param account           账号
     * @param message           消息变量
     */
    void send(Long messageTemplateId, String account, Map<String, Object> message);

    /**
     * 发送消息
     *
     * @param messageKey  消息KEY
     * @param messageType 消息类型
     * @param account     账号
     * @param message     消息变量
     */
    void send(String messageKey, MessageTypeEnum messageType, String account, Map<String, Object> message);

    /**
     * 批量发送消息
     *
     * @param messageTemplateId 消息模板id
     * @param accounts          多账号
     * @param message           消息变量
     */
    void send(Long messageTemplateId, Collection<String> accounts, Map<String, Object> message);

    /**
     * 批量发送消息
     *
     * @param messageKey  消息KEY
     * @param messageType 消息类型
     * @param accounts    多账号
     * @param message     消息变量
     */
    void send(String messageKey, MessageTypeEnum messageType, Collection<String> accounts, Map<String, Object> message);
}
