package com.ruoyi.demo.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信演示案例
 * 请先阅读文档 否则无法使用
 *
 * @author Lion Li
 * @version 4.2.0
 */
@Validated
@RestController
@RequestMapping("/demo/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;
//    private final SmsTemplate smsTemplate; // 可以使用spring注入
//    private final AliyunSmsTemplate smsTemplate; // 也可以注入某个厂家的模板工具

    /**
     * 发送短信Aliyun
     *
     * @param phones     电话号
     * @param templateId 模板ID
     */
    @GetMapping("/sendAliyun")
    public R<Object> sendAliyun(String phones, String templateId) {
        Map<String, String> map = new HashMap<>(1);
        map.put("code", "1234");
        Object send = smsService.send(phones, templateId, map);
        return R.ok(send);
    }

    /**
     * 发送短信Tencent
     *
     * @param phones     电话号
     * @param templateId 模板ID
     */
    @GetMapping("/sendTencent")
    public R<Object> sendTencent(String phones, String templateId) {
        Map<String, String> map = new HashMap<>(1);
//        map.put("2", "测试测试");
        map.put("1", "1234");
        Object send = smsService.send(phones, templateId, map);
        return R.ok(send);
    }

}
