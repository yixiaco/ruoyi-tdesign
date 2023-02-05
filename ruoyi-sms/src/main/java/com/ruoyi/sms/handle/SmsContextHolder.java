package com.ruoyi.sms.handle;

import com.ruoyi.sms.core.SmsTemplate;

/**
 * 短信对象上下文持有者
 *
 * @author hexm
 * @date 2023/2/4
 */
public class SmsContextHolder {

    private static final InheritableThreadLocal<SmsTemplate> SMS_TEMPLATE_THREAD_LOCAL_HOLDER = new InheritableThreadLocal<>();

    public static SmsTemplate getSmsTemplate() {
        return SMS_TEMPLATE_THREAD_LOCAL_HOLDER.get();
    }

    public static void setSmsTemplate(SmsTemplate smsTemplate) {
        SMS_TEMPLATE_THREAD_LOCAL_HOLDER.set(smsTemplate);
    }

    public static void removeSmsTemplate() {
        SMS_TEMPLATE_THREAD_LOCAL_HOLDER.remove();
    }
}
