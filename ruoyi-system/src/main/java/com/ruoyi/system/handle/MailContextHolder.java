package com.ruoyi.system.handle;

import cn.hutool.extra.mail.MailAccount;

/**
 * mail邮件认证对象上下文持有者
 *
 * @author hexm
 * @date 2023/2/3
 */
public class MailContextHolder {

    private static final InheritableThreadLocal<MailAccount> MAIL_ACCOUNT_THREAD_LOCAL_HOLDER = new InheritableThreadLocal<>();

    public static MailAccount getAccount() {
        return MAIL_ACCOUNT_THREAD_LOCAL_HOLDER.get();
    }

    public static void setAccount(MailAccount account) {
        MAIL_ACCOUNT_THREAD_LOCAL_HOLDER.set(account);
    }

    public static void removeAccount() {
        MAIL_ACCOUNT_THREAD_LOCAL_HOLDER.remove();
    }
}
