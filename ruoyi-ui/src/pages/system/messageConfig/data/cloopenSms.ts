import type { FieldConfig, SmsConfig } from '../model';
import { MessageConfig } from '../model';

/** 容联云短信 */
export interface CloopenSmsConfig extends SmsConfig {
  /** 应用 ID */
  sdkAppId: FieldConfig;
  /** REST API Base URL */
  baseUrl: FieldConfig;
}

/** 容联云短信配置 */
export const cloopenSms: CloopenSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'accessKeyId',
    required: true,
    help: '访问键标识',
  },
  accessKeySecret: {
    component: 'input',
    name: 'accessKeySecret',
    required: true,
    help: '访问键秘钥',
    type: 'password',
  },
  sdkAppId: {
    component: 'input',
    name: '应用 ID',
    required: true,
  },
  baseUrl: {
    value: 'https://app.cloopen.com:8883/2013-12-26',
    component: 'input',
    name: 'BaseURL',
    required: true,
  },
};

export const CloopenSmsMessageConfig = new MessageConfig(cloopenSms, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
