package org.dromara.common.sms.handle;

import org.dromara.sms4j.api.SmsBlend;

/**
 * 短信对象上下文持有者
 *
 * @author hexm
 * @date 2023/2/4
 */
public class SmsContextHolder {

    private static final InheritableThreadLocal<SmsBlend> SMS_TEMPLATE_THREAD_LOCAL_HOLDER = new InheritableThreadLocal<>();

    public static SmsBlend getSmsTemplate() {
        return SMS_TEMPLATE_THREAD_LOCAL_HOLDER.get();
    }

    public static void setSmsTemplate(SmsBlend smsTemplate) {
        SMS_TEMPLATE_THREAD_LOCAL_HOLDER.set(smsTemplate);
    }

    public static void removeSmsTemplate() {
        SMS_TEMPLATE_THREAD_LOCAL_HOLDER.remove();
    }
}
