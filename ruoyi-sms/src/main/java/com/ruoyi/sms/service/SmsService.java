package com.ruoyi.sms.service;

import com.ruoyi.sms.core.SmsTemplate;
import com.ruoyi.sms.entity.SmsResult;

import java.util.Map;

/**
 * @author hexm
 * @date 2023/2/4
 */
public interface SmsService {

    /**
     * 获取短信模板对象
     *
     * @return
     */
    SmsTemplate getSmsTemplate();

    /**
     * 发送短信
     *
     * @param phones     电话号(多个逗号分割)
     * @param templateId 模板id
     * @param param      模板对应参数
     *                   阿里 需使用 模板变量名称对应内容 例如: code=1234
     *                   腾讯 需使用 模板变量顺序对应内容 例如: 1=1234, 1为模板内第一个参数
     */
    SmsResult send(String phones, String templateId, Map<String, String> param);
}
