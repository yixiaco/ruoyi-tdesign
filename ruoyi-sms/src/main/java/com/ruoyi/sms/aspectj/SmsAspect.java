package com.ruoyi.sms.aspectj;

import com.ruoyi.sms.handle.SmsContextHolder;
import com.ruoyi.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注入Mail上下文对象
 *
 * @author hexm
 * @date 2023/2/3
 */
@Slf4j
@Aspect
@Component
public class SmsAspect {

    @Autowired
    private SmsService smsService;

    @Around("@within(smsContextCache) || @annotation(smsContextCache)")
    public Object process(ProceedingJoinPoint point, SmsContextCache smsContextCache) throws Throwable {
        try {
            SmsContextHolder.setSmsTemplate(smsService.getSmsTemplate());
            return point.proceed();
        } finally {
            SmsContextHolder.removeSmsTemplate();
        }
    }
}
