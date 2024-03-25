package org.dromara.system.helper;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.enums.MessageTypeEnum;
import org.dromara.common.core.utils.spring.SpringUtils;
import org.dromara.system.service.ISysMessageSendService;

import java.util.Collection;
import java.util.Map;

/**
 * 消息发送帮助类
 *
 * @author hexm
 * @date 2024/01/17 10:39
 */
@Slf4j
public class MessageSendHelper {

    private static final ISysMessageSendService messageSendService = SpringUtils.getBean(ISysMessageSendService.class);

    /**
     * 发送消息
     *
     * @param messageTemplateId 消息模板id
     * @param account           账号
     * @param message           消息变量
     */
    public static void send(Long messageTemplateId, String account, Map<String, Object> message) {
        messageSendService.send(messageTemplateId, account, message);
    }

    /**
     * 发送消息(忽略异常)
     *
     * @param messageTemplateId 消息模板id
     * @param account           账号
     * @param message           消息变量
     */
    public static void ignoreAndSend(Long messageTemplateId, String account, Map<String, Object> message) {
        try {
            messageSendService.send(messageTemplateId, account, message);
        } catch (Exception e) {
            log.warn("消息发送异常：{}", e.getMessage());
        }
    }

    /**
     * 发送消息
     *
     * @param messageKey  消息KEY
     * @param messageType 消息类型
     * @param account     账号
     * @param message     消息变量
     */
    public static void send(String messageKey, MessageTypeEnum messageType, String account, Map<String, Object> message) {
        messageSendService.send(messageKey, messageType, account, message);
    }

    /**
     * 发送消息(忽略异常)
     *
     * @param messageKey  消息KEY
     * @param messageType 消息类型
     * @param account     账号
     * @param message     消息变量
     */
    public static void ignoreAndSend(String messageKey, MessageTypeEnum messageType, String account, Map<String, Object> message) {
        try {
            messageSendService.send(messageKey, messageType, account, message);
        } catch (Exception e) {
            log.warn("消息发送异常：{}", e.getMessage());
        }
    }

    /**
     * 批量发送消息
     *
     * @param messageTemplateId 消息模板id
     * @param accounts          多账号
     * @param message           消息变量
     */
    public static void send(Long messageTemplateId, Collection<String> accounts, Map<String, Object> message) {
        messageSendService.send(messageTemplateId, accounts, message);
    }

    /**
     * 批量发送消息(忽略异常)
     *
     * @param messageTemplateId 消息模板id
     * @param accounts          多账号
     * @param message           消息变量
     */
    public static void ignoreAndSend(Long messageTemplateId, Collection<String> accounts, Map<String, Object> message) {
        try {
            messageSendService.send(messageTemplateId, accounts, message);
        } catch (Exception e) {
            log.warn("消息发送异常：{}", e.getMessage());
        }
    }

    /**
     * 批量发送消息
     *
     * @param messageKey  消息KEY
     * @param messageType 消息类型
     * @param accounts    多账号
     * @param message     消息变量
     */
    public static void send(String messageKey, MessageTypeEnum messageType, Collection<String> accounts, Map<String, Object> message) {
        messageSendService.send(messageKey, messageType, accounts, message);
    }

    /**
     * 批量发送消息(忽略异常)
     *
     * @param messageKey  消息KEY
     * @param messageType 消息类型
     * @param accounts    多账号
     * @param message     消息变量
     */
    public static void ignoreAndSend(String messageKey, MessageTypeEnum messageType, Collection<String> accounts, Map<String, Object> message) {
        try {
            messageSendService.send(messageKey, messageType, accounts, message);
        } catch (Exception e) {
            log.warn("消息发送异常：{}", e.getMessage());
        }
    }
}
