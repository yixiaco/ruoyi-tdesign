package org.dromara.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.dromara.common.core.enums.*;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.funtion.BiOperator;
import org.dromara.common.mail.utils.MailAccount;
import org.dromara.common.mail.utils.MailUtils;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
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
import org.dromara.system.domain.SysMessageLog;
import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.system.domain.vo.SysMessageTemplateVar;
import org.dromara.system.service.ISysMessageConfigService;
import org.dromara.system.service.ISysMessageLogService;
import org.dromara.system.service.ISysMessageSendService;
import org.dromara.system.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private ISysMessageLogService messageLogService;

    /**
     * 判断是否是html内容的正则
     */
    private static final Pattern HTML = Pattern.compile(".*((<[a-zA-Z]+[^>]*>[^<>]*</[a-zA-Z]+>)+|(<[a-zA-Z]+[^>]*/>)+).*");

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
        if (CollUtil.isEmpty(account)) {
            return;
        }
        SysMessageConfig config = messageConfigService.getById(template.getMessageConfigId());
        // 将模板变量转为Map
        List<SysMessageTemplateVar> vars = JSONUtil.toList(template.getVarsJson(), SysMessageTemplateVar.class);
        LinkedHashMap<String, String> outputVars = vars
            .stream()
            .collect(Collectors.toMap(SysMessageTemplateVar::getKey,
                SysMessageTemplateVar::getValue,
                BiOperator::first,
                LinkedHashMap::new));
        // 将模板变量的值的占位符替换为输入变量
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", true);
        Properties properties = new Properties();
        properties.putAll(message);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            entry.setValue(entry.getValue().toString());
        }
        if (!outputVars.isEmpty()) {
            outputVars.forEach((key, value) -> outputVars.put(key, helper.replacePlaceholders(value, properties)));
        }
        // 将模板内容中的占位符替换为模板变量中的值
        Properties outputProperties = new Properties();
        outputProperties.putAll(outputVars);
        String content = helper.replacePlaceholders(template.getContent(), outputProperties);
        MessageTypeEnum messageType = MessageTypeEnum.valueOf(config.getMessageType());
        MessageSupplierTypeEnum supplierType = MessageSupplierTypeEnum.valueOf(config.getSupplierType());
        // 根据消息类型分别处理
        switch (messageType) {
            case SMS -> sendSms(account, template, config, outputVars, content, supplierType);
            case MAIL -> {
                String title = helper.replacePlaceholders(template.getTitle(), properties);
                sendMail(account, template, config, title, content, supplierType);
            }
        }
    }

    /**
     * 发送短信
     */
    private void sendSms(List<String> account, SysMessageTemplate template, SysMessageConfig config, LinkedHashMap<String, String> outputVars, String content, MessageSupplierTypeEnum supplierType) {
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
        SmsResponse response;
        switch (templateMode) {
            case TEMPLATE_ID -> {
                if (account.size() == 1) {
                    response = smsBlend.sendMessage(account.get(0), template.getTemplateId(), outputVars);
                } else {
                    response = smsBlend.massTexting(account, template.getTemplateId(), outputVars);
                }
            }
            case TEMPLATE_CONTENT -> {
                if (account.size() == 1) {
                    response = smsBlend.sendMessage(account.get(0), content);
                } else {
                    response = smsBlend.massTexting(account, content);
                }
            }
            default -> throw new ServiceException("不支持的消息模板模式：" + templateMode);
        }
        // 记录发送记录
        saveLog(account, template, config, content, log -> {
            log.setIsSuccess(response.isSuccess() ? CommonStatusEnum.SUCCESS.getCodeNum() : CommonStatusEnum.FAIL.getCodeNum());
            log.setErrorCode(response.getErrorCode());
            log.setErrorMessage(response.getErrMessage());
            log.setBizId(response.getBizId());
            log.setMessage(response.getMessage());
        });
    }

    /**
     * 发送邮件
     */
    private void sendMail(List<String> account, SysMessageTemplate template, SysMessageConfig config, String title, String content, MessageSupplierTypeEnum supplierType) {
        MailAccount mailAccount = switch (supplierType) {
            case MAIL -> JSONUtil.toBean(config.getConfigJson(), MailAccount.class);
            default -> throw new ServiceException("不支持的消息类型");
        };
        String messageId;
        if (isHtml(content)) {
            messageId = MailUtils.sendHtml(mailAccount, account, title, content);
        } else {
            messageId = MailUtils.sendText(mailAccount, account, title, content);
        }
        // 记录发送记录
        saveLog(account, template, config, content, log -> {
            log.setIsSuccess(CommonStatusEnum.SUCCESS.getCodeNum());
            log.setBizId(messageId);
            log.setTitle(title);
        });
    }

    /**
     * 是否是html
     *
     * @param content 内容
     */
    private static boolean isHtml(String content) {
        return HTML.matcher(content).matches();
    }

    /**
     * 保存发送记录
     *
     * @param account  发送账号
     * @param template 模板对象
     * @param config   配置对象
     * @param content  内容
     * @param consumer 日志对象的回调变更
     */
    private void saveLog(List<String> account, SysMessageTemplate template, SysMessageConfig config, String content, Consumer<SysMessageLog> consumer) {
        // 记录发送记录
        List<SysMessageLog> logs = account.stream().map(mobile -> {
            SysMessageLog log = new SysMessageLog();
            log.setMessageTemplateId(template.getMessageTemplateId());
            log.setMessageKey(template.getMessageKey());
            log.setMessageTemplateName(template.getTemplateName());
            log.setMessageType(template.getMessageType());
            log.setTemplateMode(template.getTemplateMode());
            log.setAccount(account.get(0));
            log.setTemplateId(template.getTemplateId());
            log.setContent(content);
            log.setMessageConfigTitle(config.getTitle());
            log.setSupplierType(config.getSupplierType());
            if (consumer != null) {
                consumer.accept(log);
            }
            log.setLogTime(new Date());
            return log;
        }).toList();
        messageLogService.saveBatch(logs);
    }
}
