package org.dromara;

import cn.hutool.crypto.SecureUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 启动程序
 *
 * @author ruoyi
 */

@SpringBootApplication
public class DromaraApplication {

    public static void main(String[] args) {
        SecureUtil.disableBouncyCastle();
        SpringApplication application = new SpringApplication(DromaraApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  ruoyi-tdesign启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
