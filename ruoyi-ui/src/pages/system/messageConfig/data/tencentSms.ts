import type { FieldConfig, SignatureSmsConfig } from '../model';
import { MessageConfig } from '../model';

/** 腾讯云短信 */
export interface TencentSmsConfig extends SignatureSmsConfig {
  /** 短信sdkAppId */
  sdkAppId: FieldConfig;
  /** 地域信息默认为 ap-guangzhou */
  territory: FieldConfig;
  /** 请求超时时间 */
  connTimeout: FieldConfig<number>;
  /** 请求地址 */
  requestUrl: FieldConfig;
  /** 接口名称 */
  action: FieldConfig;
  /** 接口版本 */
  version: FieldConfig;
}

/** 腾讯云短信配置 */
export const tencent: TencentSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'accessKey',
    help: '腾讯云的accessKey。SecretId、SecretKey 查询: https://console.cloud.tencent.com/cam/capi',
    required: true,
  },
  accessKeySecret: {
    component: 'input',
    name: 'accessKeySecret',
    help: '腾讯云的accessKeySecret',
    required: true,
    type: 'password',
  },
  signature: {
    component: 'input',
    name: '默认短信签名',
    required: true,
    help:
      '签名信息可前往 [国内短信](https://console.cloud.tencent.com/smsv2/csms-sign) 或<br />' +
      '[国际/港澳台短信](https://console.cloud.tencent.com/smsv2/isms-sign) 的签名管理查看',
  },
  sdkAppId: {
    component: 'input',
    name: '短信应用ID',
    help: '短信sdkAppId。应用 ID 可前往 [短信控制台](https://console.cloud.tencent.com/smsv2/app-manage) 查看',
    required: true,
  },
  territory: {
    value: 'ap-guangzhou',
    component: 'input',
    name: '地域',
    help: '地域信息默认为 ap-guangzhou 如无特殊改变可不用设置',
    required: true,
  },
  connTimeout: {
    value: 60,
    component: 'input-number',
    name: '请求超时时间',
    help: '请求超时时间 默认60秒',
    required: true,
    min: 30,
    max: 180,
  },
  requestUrl: {
    value: 'sms.tencentcloudapi.com',
    component: 'input',
    name: '请求地址',
    help: '请求地址默认为 sms.tencentcloudapi.com 如无特殊改变可不用设置',
    required: true,
  },
  action: {
    value: 'SendSms',
    component: 'input',
    name: '接口方法',
    required: true,
    help: '接口方法默认为 SendSms 如无特殊改变可以不用设置',
  },
  version: {
    value: '2021-01-11',
    component: 'input',
    name: '接口版本号',
    required: true,
    help: '接口版本默认为 2021-01-11 如无特殊改变可不用设置',
  },
};

export const TencentMessageConfig = new MessageConfig(tencent, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
