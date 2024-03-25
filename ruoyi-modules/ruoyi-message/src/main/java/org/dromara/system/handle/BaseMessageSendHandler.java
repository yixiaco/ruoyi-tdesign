package org.dromara.system.handle;

import cn.hutool.json.JSONUtil;
import org.dromara.common.core.utils.LocalTimeInterval;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.funtion.BiOperator;
import org.dromara.system.domain.SysMessageConfig;
import org.dromara.system.domain.SysMessageLog;
import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.system.domain.vo.SysMessageTemplateVar;
import org.dromara.system.service.ISysMessageLogService;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 基本消息发送处理类
 *
 * @author hexm
 * @date 2023/07/24 10:43
 */
public abstract class BaseMessageSendHandler implements MessageSendHandler {

    /**
     * Prefix for property placeholders: "${".
     */
    public static final String PLACEHOLDER_PREFIX = "${";

    /**
     * Suffix for property placeholders: "}".
     */
    public static final String PLACEHOLDER_SUFFIX = "}";

    /**
     * Value separator for property placeholders: ":".
     */
    public static final String VALUE_SEPARATOR = ":";

    protected static final PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, VALUE_SEPARATOR, true);

    protected ISysMessageLogService messageLogService;

    public BaseMessageSendHandler(ISysMessageLogService messageLogService) {
        this.messageLogService = messageLogService;
    }

    /**
     * 获取输出变量
     *
     * @param template 模板对象
     * @param message  未转换的消息体
     * @return 返回经过转换的消息变量
     */
    protected LinkedHashMap<String, String> getOutputVars(SysMessageTemplate template, Map<String, Object> message) {
        // 将模板变量转为Map
        List<SysMessageTemplateVar> vars = JSONUtil.toList(template.getVarsJson(), SysMessageTemplateVar.class);
        LinkedHashMap<String, String> outputVars = vars
            .stream()
            .collect(Collectors.toMap(SysMessageTemplateVar::getKey,
                SysMessageTemplateVar::getValue,
                BiOperator::first,
                LinkedHashMap::new));
        // 将模板变量的值的占位符替换为输入变量
        Properties properties = getProperties(message);
        if (!outputVars.isEmpty()) {
            outputVars.forEach((key, value) -> outputVars.put(key, helper.replacePlaceholders(value, properties)));
        }
        return outputVars;
    }

    /**
     * 将message转为Properties对象
     *
     * @param message 未转换的消息体
     * @return
     */
    protected static Properties getProperties(Map<String, Object> message) {
        Properties properties = new Properties();
        for (Map.Entry<String, Object> entry : message.entrySet()) {
            properties.put(entry.getKey(), Objects.requireNonNullElse(entry.getValue(), StringUtils.EMPTY).toString());
        }
        return properties;
    }

    /**
     * 获取渲染后的消息内容
     *
     * @param template   模板对象
     * @param outputVars 经过转换的消息变量
     * @return 消息内容
     */
    protected String getContent(SysMessageTemplate template, LinkedHashMap<String, String> outputVars) {
        // 将模板内容中的占位符替换为模板变量中的值
        Properties outputProperties = new Properties();
        outputProperties.putAll(outputVars);
        return helper.replacePlaceholders(template.getContent(), outputProperties);
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
    protected void saveLog(String account, SysMessageTemplate template, SysMessageConfig config, String content, Consumer<SysMessageLog> consumer) {
        saveLog(Collections.singleton(account), template, config, content, consumer);
    }

    /**
     * 保存发送记录
     *
     * @param accounts 发送账号
     * @param template 模板对象
     * @param config   配置对象
     * @param content  内容
     * @param consumer 日志对象的回调变更
     */
    protected void saveLog(Collection<String> accounts, SysMessageTemplate template, SysMessageConfig config, String content, Consumer<SysMessageLog> consumer) {
        Long currentInterval = LocalTimeInterval.getCurrentIntervalAndClear();
        // 记录发送记录
        List<SysMessageLog> logs = accounts.stream().map(account -> {
            SysMessageLog log = new SysMessageLog();
            log.setMessageTemplateId(template.getMessageTemplateId());
            log.setMessageKey(template.getMessageKey());
            log.setMessageTemplateName(template.getTemplateName());
            log.setMessageType(template.getMessageType());
            log.setTemplateMode(template.getTemplateMode());
            log.setAccount(account);
            log.setTemplateId(template.getTemplateId());
            log.setContent(content);
            log.setMessageConfigTitle(config.getTitle());
            log.setSupplierType(config.getSupplierType());
            if (consumer != null) {
                consumer.accept(log);
            }
            log.setLogTime(new Date());
            log.setCostTime(currentInterval);
            return log;
        }).toList();
        messageLogService.saveBatch(logs);
    }
}
