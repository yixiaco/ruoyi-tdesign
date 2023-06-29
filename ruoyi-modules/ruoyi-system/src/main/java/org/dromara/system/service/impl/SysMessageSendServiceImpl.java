package org.dromara.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.dromara.common.core.enums.MessageSupplierTypeEnum;
import org.dromara.common.core.enums.MessageTemplateMode;
import org.dromara.common.core.enums.MessageTypeEnum;
import org.dromara.common.core.enums.NormalDisableEnum;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mail.utils.MailAccount;
import org.dromara.common.mail.utils.MailUtils;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.provider.enumerate.SupplierType;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;
import org.dromara.system.domain.SysMessageConfig;
import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.system.service.ISysMessageConfigService;
import org.dromara.system.service.ISysMessageSendService;
import org.dromara.system.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

    /**
     * 发送消息
     *
     * @param messageTemplateId 消息模板id
     * @param account           账号
     * @param message           消息变量
     */
    @Override
    public void send(Long messageTemplateId, String account, Map<String, Object> message) {
        SysMessageTemplate template = messageTemplateService.getById(messageTemplateId);
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
        SysMessageTemplate template = messageTemplateService.lambdaQuery()
            .eq(SysMessageTemplate::getMessageKey, messageKey)
            .eq(SysMessageTemplate::getMessageType, messageType.name())
            .eq(SysMessageTemplate::getStatus, NormalDisableEnum.NORMAL.getCode())
            .one();
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
    public void send(Long messageTemplateId, List<String> accounts, Map<String, Object> message) {
        SysMessageTemplate template = messageTemplateService.getById(messageTemplateId);
        if (template == null) {
            throw new ServiceException("消息模板不存在");
        }
        send(accounts, message, template);
    }

    /**
     * 发送消息
     *
     * @param messageKey  消息KEY
     * @param messageType 消息类型
     * @param accounts    多账号
     * @param message     消息变量
     */
    @Override
    public void send(String messageKey, MessageTypeEnum messageType, List<String> accounts, Map<String, Object> message) {
        SysMessageTemplate template = messageTemplateService.lambdaQuery()
            .eq(SysMessageTemplate::getMessageKey, messageKey)
            .eq(SysMessageTemplate::getMessageType, messageType.name())
            .eq(SysMessageTemplate::getStatus, NormalDisableEnum.NORMAL.getCode())
            .one();
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
    private void send(List<String> account, Map<String, Object> message, SysMessageTemplate template) {
        SysMessageConfig config = messageConfigService.getById(template.getMessageConfigId());
        LinkedHashMap<String, String> outputVars = JsonUtils.parseObject(template.getVarsJson(), new TypeReference<>() {
        });
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", true);
        Properties properties = new Properties();
        properties.putAll(message);
        if (outputVars != null) {
            outputVars.forEach((key, value) -> outputVars.put(key, helper.replacePlaceholders(value, properties)));
        }
        Properties outputProperties = new Properties();
        outputProperties.putAll(outputVars);
        MessageSupplierTypeEnum supplierType = MessageSupplierTypeEnum.valueOf(config.getSupplierType());
        MessageTypeEnum messageType = MessageTypeEnum.valueOf(config.getMessageType());
        switch (messageType) {
            case SMS -> {
                SmsBlend smsBlend = switch (supplierType) {
                    case ALIBABA -> {
                        AlibabaConfig alibabaConfig = JSONUtil.toBean(config.getConfigJson(), AlibabaConfig.class, true);
                        if (StrUtil.isNotBlank(template.getSignature())) {
                            alibabaConfig.setSignature(template.getSignature());
                        }
                        yield SmsFactory.createSmsBlend(SupplierType.ALIBABA, alibabaConfig);
                    }
                    case HUAWEI -> {
                        HuaweiConfig huaweiConfig = JSONUtil.toBean(config.getConfigJson(), HuaweiConfig.class, true);
                        if (StrUtil.isNotBlank(template.getSignature())) {
                            huaweiConfig.setSignature(template.getSignature());
                        }
                        yield SmsFactory.createSmsBlend(SupplierType.HUAWEI, huaweiConfig);
                    }
                    case TENCENT -> {
                        TencentConfig tencentConfig = JSONUtil.toBean(config.getConfigJson(), TencentConfig.class, true);
                        if (StrUtil.isNotBlank(template.getSignature())) {
                            tencentConfig.setSignature(template.getSignature());
                        }
                        yield SmsFactory.createSmsBlend(SupplierType.TENCENT, tencentConfig);
                    }
                    case YUNPIAN -> {
                        YunpianConfig yunpianConfig = JSONUtil.toBean(config.getConfigJson(), YunpianConfig.class, true);
                        if (StrUtil.isNotBlank(template.getSignature())) {
                            yunpianConfig.setSignature(template.getSignature());
                        }
                        yield SmsFactory.createSmsBlend(SupplierType.YUNPIAN, yunpianConfig);
                    }
                    case UNI_SMS -> {
                        UniConfig uniConfig = JSONUtil.toBean(config.getConfigJson(), UniConfig.class, true);
                        if (StrUtil.isNotBlank(template.getSignature())) {
                            uniConfig.setSignature(template.getSignature());
                        }
                        yield SmsFactory.createSmsBlend(SupplierType.UNI_SMS, uniConfig);
                    }
                    case JD_CLOUD -> {
                        JdCloudConfig jdCloudConfig = JSONUtil.toBean(config.getConfigJson(), JdCloudConfig.class, true);
                        if (StrUtil.isNotBlank(template.getSignature())) {
                            jdCloudConfig.setSignature(template.getSignature());
                        }
                        yield SmsFactory.createSmsBlend(SupplierType.JD_CLOUD, jdCloudConfig);
                    }
                    case CLOOPEN -> {
                        CloopenConfig cloopenConfig = JSONUtil.toBean(config.getConfigJson(), CloopenConfig.class, true);
                        if (StrUtil.isNotBlank(template.getSignature())) {
                            cloopenConfig.setSignature(template.getSignature());
                        }
                        yield SmsFactory.createSmsBlend(SupplierType.CLOOPEN, cloopenConfig);
                    }
                    case EMAY -> {
                        EmayConfig emayConfig = JSONUtil.toBean(config.getConfigJson(), EmayConfig.class, true);
                        yield SmsFactory.createSmsBlend(SupplierType.EMAY, emayConfig);
                    }
                    case CTYUN -> {
                        CtyunConfig ctyunConfig = JSONUtil.toBean(config.getConfigJson(), CtyunConfig.class, true);
                        if (StrUtil.isNotBlank(template.getSignature())) {
                            ctyunConfig.setSignature(template.getSignature());
                        }
                        yield SmsFactory.createSmsBlend(SupplierType.CTYUN, ctyunConfig);
                    }
                    case NETEASE -> {
                        NeteaseConfig neteaseConfig = JSONUtil.toBean(config.getConfigJson(), NeteaseConfig.class, true);
                        if (StrUtil.isNotBlank(template.getSignature())) {
                            neteaseConfig.setSignature(template.getSignature());
                        }
                        yield SmsFactory.createSmsBlend(SupplierType.NETEASE, neteaseConfig);
                    }
                    default -> throw new ServiceException("不支持的消息类型");
                };
                MessageTemplateMode templateMode = MessageTemplateMode.valueOf(template.getTemplateMode());
                // 消息发送方式
                switch (templateMode) {
                    case TEMPLATE_ID -> {
                        if (account.size() == 1) {
                            smsBlend.sendMessage(account.get(0), template.getTemplateId(), outputVars);
                        } else {
                            smsBlend.massTexting(account, template.getTemplateId(), outputVars);
                        }
                    }
                    case TEMPLATE_CONTENT -> {
                        String content = helper.replacePlaceholders(template.getContent(), outputProperties);
                        if (account.size() == 1) {
                            smsBlend.sendMessage(account.get(0), content);
                        } else {
                            smsBlend.massTexting(account, content);
                        }
                    }
                }
            }
            case MAIL -> {
                MailAccount mailAccount = switch (supplierType) {
                    case MAIL -> JSONUtil.toBean(config.getConfigJson(), MailAccount.class);
                    default -> throw new ServiceException("不支持的消息类型");
                };
                String content = helper.replacePlaceholders(template.getContent(), outputProperties);
                if (content.contains("<") && content.contains(">")) {
                    MailUtils.sendHtml(mailAccount, account, template.getTitle(), content);
                } else {
                    MailUtils.sendText(mailAccount, account, template.getTitle(), content);
                }
            }
        }
    }
}
