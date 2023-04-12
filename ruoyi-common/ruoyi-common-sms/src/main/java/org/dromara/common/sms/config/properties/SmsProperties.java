package org.dromara.common.sms.config.properties;

import lombok.Data;

/**
 * SMS短信 配置属性
 *
 * @author Lion Li
 * @version 4.2.0
 */
@Data
public class SmsProperties {

    private Boolean enabled;

    /**
     * 配置节点
     * 阿里云 dysmsapi.aliyuncs.com
     * 腾讯云 sms.tencentcloudapi.com
     * 请勿修改地域域名，暂不支持其他地域域名
     */
    private String endpoint;

    /**
     * key
     */
    private String accessKeyId;

    /**
     * 密匙
     */
    private String accessKeySecret;

    /*
     * 短信签名
     */
    private String signName;

    /**
     * 短信应用ID (腾讯专属)
     */
    private String sdkAppId;

}
