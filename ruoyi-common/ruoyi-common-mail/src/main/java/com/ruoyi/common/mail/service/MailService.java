package com.ruoyi.common.mail.service;

import cn.hutool.extra.mail.MailAccount;

import jakarta.mail.Session;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

/**
 * mail服务
 *
 * @author hexm
 * @date 2023/02/03 14:51
 */
public interface MailService {

    /**
     * 获取邮件发送实例
     *
     * @return
     */
    MailAccount getAccount();

    /**
     * 获取邮件发送实例 (自定义发送人以及授权码)
     *
     * @param from 发送方
     * @param user 用户名
     * @param pass 授权码
     */
    MailAccount getMailAccount(String from, String user, String pass);

    /**
     * 使用配置文件中设置的账户发送文本邮件，发送给单个或多个收件人<br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     * @since 3.2.0
     */
    String sendText(String to, String subject, String content, File... files);

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给单个或多个收件人<br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     * @since 3.2.0
     */
    String sendHtml(String to, String subject, String content, File... files);

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
     * 多个收件人、抄送人、密送人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param cc      抄送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param bcc     密送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML
     * @param files   附件列表
     * @return message-id
     * @since 4.0.3
     */
    String send(String to, String cc, String bcc, String subject, String content, boolean isHtml, File... files);

    /**
     * 使用配置文件中设置的账户发送文本邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     */
    String sendText(Collection<String> tos, String subject, String content, File... files);

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     * @since 3.2.0
     */
    String sendHtml(Collection<String> tos, String subject, String content, File... files);

    // ------------------------------------------------------------------------------------------------------------------------------- Custom MailAccount

    /**
     * 发送邮件给多人
     *
     * @param to      收件人，多个收件人逗号或者分号隔开
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML格式
     * @param files   附件列表
     * @return message-id
     * @since 3.2.0
     */
    String send(String to, String subject, String content, boolean isHtml, File... files);

    /**
     * 发送邮件给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML格式
     * @param files   附件列表
     * @return message-id
     */
    String send(Collection<String> tos, String subject, String content, boolean isHtml, File... files);

    /**
     * 发送邮件给多人
     *
     * @param tos     收件人列表
     * @param ccs     抄送人列表，可以为null或空
     * @param bccs    密送人列表，可以为null或空
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML格式
     * @param files   附件列表
     * @return message-id
     * @since 4.0.3
     */
    String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, boolean isHtml, File... files);

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给单个或多个收件人<br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to       收件人
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param files    附件列表
     * @return message-id
     * @since 3.2.0
     */
    String sendHtml(String to, String subject, String content, Map<String, InputStream> imageMap, File... files);

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br>
     * 多个收件人、抄送人、密送人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to       收件人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param cc       抄送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param bcc      密送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML
     * @param files    附件列表
     * @return message-id
     * @since 4.0.3
     */
    String send(String to, String cc, String bcc, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files);

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给多人
     *
     * @param tos      收件人列表
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param files    附件列表
     * @return message-id
     * @since 3.2.0
     */
    String sendHtml(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, File... files);

    // ------------------------------------------------------------------------------------------------------------------------------- Custom MailAccount

    /**
     * 发送邮件给多人
     *
     * @param to       收件人，多个收件人逗号或者分号隔开
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML格式
     * @param files    附件列表
     * @return message-id
     * @since 3.2.0
     */
    String send(String to, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files);

    /**
     * 发送邮件给多人
     *
     * @param tos      收件人列表
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML格式
     * @param files    附件列表
     * @return message-id
     * @since 4.6.3
     */
    String send(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files);

    /**
     * 发送邮件给多人
     *
     * @param tos      收件人列表
     * @param ccs      抄送人列表，可以为null或空
     * @param bccs     密送人列表，可以为null或空
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML格式
     * @param files    附件列表
     * @return message-id
     * @since 4.6.3
     */
    String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, Map<String, InputStream> imageMap,
                boolean isHtml, File... files);

    /**
     * 根据配置文件，获取邮件客户端会话
     *
     * @param isSingleton 是否单例（全局共享会话）
     * @return {@link Session}
     * @since 5.5.7
     */
    Session getSession(boolean isSingleton);
}
