package com.ruoyi.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.config.MailProperties;
import com.ruoyi.common.core.service.ConfigService;
import com.ruoyi.common.core.service.MailService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.email.MailUtils;
import com.ruoyi.system.handle.MailContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

/**
 * mail服务
 * 当执行多次的接口调用时，可以手动缓存对象到{@linkplain  MailContextHolder}中，并在执行结束后移除缓存，避免修改配置后读取不到；
 * 或者可以使用{@linkplain com.ruoyi.system.aspectj.MailContextCache}注解在使用的SpringBean中使用，可以在整个方法中缓存发送对象
 *
 * @author hexm
 * @date 2023/02/03 15:19
 */
@Service
public class MailServiceImpl implements MailService {

    /** 存储key常量 */
    public static final String SYS_MAIL_KEY = "sys.mail";
    @Autowired
    private ConfigService configService;

    @Override
    public MailAccount getAccount() {
        // 优先从上下文中获取
        MailAccount account = MailContextHolder.getAccount();
        if (account != null) {
            return account;
        }
        String mailJson = configService.getConfigValue(SYS_MAIL_KEY);
        if (StrUtil.isBlank(mailJson)) {
            throw new ServiceException("邮箱未配置！");
        }
        MailProperties properties = JSONUtil.toBean(mailJson, MailProperties.class);
        if (properties.getEnabled()) {
            account = new MailAccount();
            account.setHost(properties.getHost());
            account.setPort(properties.getPort());
            account.setAuth(properties.getAuth());
            account.setFrom(properties.getFrom());
            account.setUser(properties.getUser());
            account.setPass(properties.getPass());
            account.setSocketFactoryPort(properties.getPort());
            account.setStarttlsEnable(properties.getStarttlsEnable());
            account.setSslEnable(properties.getSslEnable());
            account.setTimeout(properties.getTimeout());
            account.setConnectionTimeout(properties.getConnectionTimeout());
            return account;
        }
        throw new ServiceException("邮箱未启用！");
    }

    /**
     * 获取邮件发送实例 (自定义发送人以及授权码)
     *
     * @param from 发送方
     * @param user 用户名
     * @param pass 授权码
     */
    @Override
    public MailAccount getMailAccount(String from, String user, String pass) {
        return MailUtils.getMailAccount(getAccount(), from, user, pass);
    }

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
    @Override
    public String sendText(String to, String subject, String content, File... files) {
        return MailUtils.sendText(getAccount(), to, subject, content, files);
    }

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
    @Override
    public String sendHtml(String to, String subject, String content, File... files) {
        return MailUtils.sendHtml(getAccount(), to, subject, content, files);
    }

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
    @Override
    public String send(String to, String cc, String bcc, String subject, String content, boolean isHtml, File... files) {
        return MailUtils.send(getAccount(), to, cc, bcc, subject, content, isHtml, files);
    }

    /**
     * 使用配置文件中设置的账户发送文本邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     */
    @Override
    public String sendText(Collection<String> tos, String subject, String content, File... files) {
        return MailUtils.sendText(getAccount(), tos, subject, content, files);
    }

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
    @Override
    public String sendHtml(Collection<String> tos, String subject, String content, File... files) {
        return MailUtils.sendHtml(getAccount(), tos, subject, content, files);
    }

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
    @Override
    public String send(String to, String subject, String content, boolean isHtml, File... files) {
        return MailUtils.send(getAccount(), to, subject, content, isHtml, files);
    }

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
    @Override
    public String send(Collection<String> tos, String subject, String content, boolean isHtml, File... files) {
        return MailUtils.send(getAccount(), tos, subject, content, isHtml, files);
    }

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
    @Override
    public String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, boolean isHtml, File... files) {
        return MailUtils.send(getAccount(), tos, ccs, bccs, subject, content, isHtml, files);
    }

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
    @Override
    public String sendHtml(String to, String subject, String content, Map<String, InputStream> imageMap, File... files) {
        return MailUtils.sendHtml(getAccount(), to, subject, content, imageMap, files);
    }

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
    @Override
    public String send(String to, String cc, String bcc, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return MailUtils.send(getAccount(), to, cc, bcc, subject, content, imageMap, isHtml, files);
    }

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
    @Override
    public String sendHtml(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, File... files) {
        return MailUtils.sendHtml(getAccount(), tos, subject, content, imageMap, files);
    }

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
    @Override
    public String send(String to, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return MailUtils.send(getAccount(), to, subject, content, imageMap, isHtml, files);
    }

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
    @Override
    public String send(Collection<String> tos, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return MailUtils.send(getAccount(), tos, subject, content, imageMap, isHtml, files);
    }

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
    @Override
    public String send(Collection<String> tos, Collection<String> ccs, Collection<String> bccs, String subject, String content, Map<String, InputStream> imageMap, boolean isHtml, File... files) {
        return MailUtils.send(getAccount(), tos, ccs, bccs, subject, content, imageMap, isHtml, files);
    }

    /**
     * 根据配置文件，获取邮件客户端会话
     *
     * @param isSingleton 是否单例（全局共享会话）
     * @return {@link Session}
     * @since 5.5.7
     */
    @Override
    public Session getSession(boolean isSingleton) {
        return MailUtils.getSession(getAccount(), isSingleton);
    }
}
