package com.ruoyi.web.controller.common;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;

/**
 * @author hexm
 * @date 2023/02/03 14:33
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    /**
     * 发送测试邮件
     *
     * @param toMail
     * @return
     */
    @PostMapping("/sendTestMail")
    public R<Void> sendTestMail(@NotBlank(message = "收件人地址不能为空") String toMail) {
        mailService.sendText(toMail, "Mail配置测试邮件", "Mail配置测试邮件");
        return R.ok();
    }
}
