package com.ruoyi.common.sms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.helper.SysConfigHelper;
import com.ruoyi.common.json.utils.JsonUtils;
import com.ruoyi.common.sms.config.properties.SmsProperties;
import com.ruoyi.common.sms.core.AliyunSmsTemplate;
import com.ruoyi.common.sms.core.SmsTemplate;
import com.ruoyi.common.sms.core.TencentSmsTemplate;
import com.ruoyi.common.sms.entity.SmsResult;
import com.ruoyi.common.sms.handle.SmsContextHolder;
import com.ruoyi.common.sms.service.SmsService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信服务
 * 当执行多次的接口调用时，可以手动缓存对象到{@linkplain  SmsContextHolder}中，并在执行结束后移除缓存，避免修改配置后读取不到；
 * 或者可以使用{@linkplain com.ruoyi.common.sms.aspectj.SmsContextCache}注解在使用的SpringBean中使用，可以在整个方法中缓存发送对象
 * @author hexm
 * @date 2023/2/4
 */
@Service
public class SmsServiceImpl implements SmsService {

    @Override
    public SmsTemplate getSmsTemplate() {
        // 优先从上下文中获取
        SmsTemplate template = SmsContextHolder.getSmsTemplate();
        if (template != null) {
            return template;
        }
        String smsJson = SysConfigHelper.getSysSms();
        if (StrUtil.isBlank(smsJson)) {
            throw new ServiceException("当前系统未配置短信功能！");
        }
        SmsProperties properties = JsonUtils.parseObject(smsJson, SmsProperties.class);
        if (properties != null && properties.getEnabled()) {
            template = switch (properties.getEndpoint()) {
                case "dysmsapi.aliyuncs.com" -> new AliyunSmsTemplate(properties);
                case "sms.tencentcloudapi.com" -> new TencentSmsTemplate(properties);
                default -> throw new ServiceException("未找到对应的短信平台！");
            };
            return template;
        }
        throw new ServiceException("当前系统没有开启短信功能！");
    }

    @Override
    public SmsResult send(String phones, String templateId, Map<String, String> param) {
        return getSmsTemplate().send(phones, templateId, param);
    }
}
