package com.ruoyi.common.mail.aspectj;

import com.ruoyi.common.mail.handle.MailContextHolder;
import com.ruoyi.common.mail.service.MailService;
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
public class MailAspect {

    @Autowired
    private MailService mailService;

    @Around("@within(mailContextCache) || @annotation(mailContextCache)")
    public Object process(ProceedingJoinPoint point, MailContextCache mailContextCache) throws Throwable {
        try {
            MailContextHolder.setAccount(mailService.getAccount());
            return point.proceed();
        } finally {
            MailContextHolder.removeAccount();
        }
    }
}
