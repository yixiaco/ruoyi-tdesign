package org.dromara.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.dromara.common.core.enums.MessageTypeEnum;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.LocalTimeInterval;
import org.dromara.system.domain.SysMessageConfig;
import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.system.handle.MessageSendHandler;
import org.dromara.system.service.ISysMessageConfigService;
import org.dromara.system.service.ISysMessageSendService;
import org.dromara.system.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 消息发送 服务实现
 *
 * @author hexm
 * @date 2023/06/29 16:15
 */
@Service
public class SysMessageSendServiceImpl implements ISysMessageSendService {

    @Autowired
    private ISysMessageTemplateService messageTemplateService;
    @Autowired
    private ISysMessageConfigService messageConfigService;
    @Autowired
    private List<MessageSendHandler> messageSendHandlers;

    /**
     * 发送消息
     *
     * @param messageTemplateId 消息模板id
     * @param account           账号
     * @param message           消息变量
     */
    @Override
    public void send(Long messageTemplateId, String account, Map<String, Object> message) {
        LocalTimeInterval.start();
        SysMessageTemplate template = messageTemplateService.getCacheById(messageTemplateId);
        if (template == null) {
            throw new ServiceException("消息模板不存在");
        }
        send(List.of(account), message, template);
    }

    /**
     * 发送消息
     *
     * @param messageKey  消息KEY
     * @param messageType 消息类型
     * @param account     账号
     * @param message     消息变量
     */
    @Override
    public void send(String messageKey, MessageTypeEnum messageType, String account, Map<String, Object> message) {
        LocalTimeInterval.start();
        SysMessageTemplate template = messageTemplateService.getCache(messageType.name(), messageKey);
        if (template == null) {
            throw new ServiceException("消息模板不存在");
        }
        send(List.of(account), message, template);
    }

    /**
     * 批量发送消息
     *
     * @param messageTemplateId 消息模板id
     * @param accounts          多账号
     * @param message           消息变量
     */
    @Override
    public void send(Long messageTemplateId, Collection<String> accounts, Map<String, Object> message) {
        LocalTimeInterval.start();
        SysMessageTemplate template = messageTemplateService.getCacheById(messageTemplateId);
        if (template == null) {
            throw new ServiceException("消息模板不存在");
        }
        send(accounts, message, template);
    }

    /**
     * 批量发送消息
     *
     * @param messageKey  消息KEY
     * @param messageType 消息类型
     * @param accounts    多账号
     * @param message     消息变量
     */
    @Override
    public void send(String messageKey, MessageTypeEnum messageType, Collection<String> accounts, Map<String, Object> message) {
        LocalTimeInterval.start();
        SysMessageTemplate template = messageTemplateService.getCache(messageType.name(), messageKey);
        if (template == null) {
            throw new ServiceException("消息模板不存在");
        }
        send(accounts, message, template);
    }

    /**
     * 发送消息
     *
     * @param account  账号
     * @param message  消息变量
     * @param template 模板对象
     */
    private void send(Collection<String> account, Map<String, Object> message, SysMessageTemplate template) {
        if (CollUtil.isEmpty(account)) {
            return;
        }
        SysMessageConfig config = messageConfigService.getCacheById(template.getMessageConfigId());
        MessageTypeEnum messageType = MessageTypeEnum.valueOf(config.getMessageType());
        try {
            for (MessageSendHandler handler : messageSendHandlers) {
                if (handler.getMessageType() == messageType) {
                    handler.send(account, message, template, config);
                    break;
                }
            }
        } finally {
            LocalTimeInterval.clear();
        }
    }
}
