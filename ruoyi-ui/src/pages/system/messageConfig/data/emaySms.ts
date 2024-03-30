import type { FieldConfig, SmsConfig } from '../model';
import { MessageConfig } from '../model';

/** 亿美软通短信 */
export interface EmaySmsConfig extends SmsConfig {
  /** APP接入地址 */
  requestUrl: FieldConfig;
}

/** 亿美软通短信配置 */
export const emaySmsConfig: EmaySmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'appId',
    required: true,
    help: '访问键标识',
  },
  accessKeySecret: {
    component: 'input',
    name: 'secretKey',
    required: true,
    help: '访问键秘钥',
    type: 'password',
  },
  requestUrl: {
    component: 'input',
    name: '请求地址',
    required: true,
    help: '短信发送请求地址',
  },
};

export const EmaySmsMessageConfig = new MessageConfig(emaySmsConfig, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
