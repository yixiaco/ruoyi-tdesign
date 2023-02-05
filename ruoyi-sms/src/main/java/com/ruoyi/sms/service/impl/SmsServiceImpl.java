package com.ruoyi.sms.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.service.ConfigService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.sms.config.properties.SmsProperties;
import com.ruoyi.sms.core.AliyunSmsTemplate;
import com.ruoyi.sms.core.SmsTemplate;
import com.ruoyi.sms.core.TencentSmsTemplate;
import com.ruoyi.sms.entity.SmsResult;
import com.ruoyi.sms.handle.SmsContextHolder;
import com.ruoyi.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信服务
 * 当执行多次的接口调用时，可以手动缓存对象到{@linkplain  SmsContextHolder}中，并在执行结束后移除缓存，避免修改配置后读取不到；
 * 或者可以使用{@linkplain com.ruoyi.sms.aspectj.SmsContextCache}注解在使用的SpringBean中使用，可以在整个方法中缓存发送对象
 * @author hexm
 * @date 2023/2/4
 */
@Service
public class SmsServiceImpl implements SmsService {

    /** 存储key常量 */
    public static final String SYS_MAIL_KEY = "sys.sms";
    @Autowired
    private ConfigService configService;

    @Override
    public SmsTemplate getSmsTemplate() {
        // 优先从上下文中获取
        SmsTemplate template = SmsContextHolder.getSmsTemplate();
        if (template != null) {
            return template;
        }
        String smsJson = configService.getConfigValue(SYS_MAIL_KEY);
        if (StrUtil.isBlank(smsJson)) {
            throw new ServiceException("当前系统未配置短信功能！");
        }
        SmsProperties properties = JSONUtil.toBean(smsJson, SmsProperties.class);
        if (properties.getEnabled()) {
            switch (properties.getEndpoint()) {
                case "dysmsapi.aliyuncs.com":
                    template = new AliyunSmsTemplate(properties);
                    break;
                case "sms.tencentcloudapi.com":
                    template = new TencentSmsTemplate(properties);
                    break;
                default:
                    throw new ServiceException("未找到对应的短信平台！");
            }
            return template;
        }
        throw new ServiceException("当前系统没有开启短信功能！");
    }

    @Override
    public SmsResult send(String phones, String templateId, Map<String, String> param) {
        return getSmsTemplate().send(phones, templateId, param);
    }
}
