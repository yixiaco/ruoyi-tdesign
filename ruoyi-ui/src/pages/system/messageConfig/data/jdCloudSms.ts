import type { FieldConfig, SignatureSmsConfig } from '../model';
import { MessageConfig } from '../model';

/** 京东云短信 */
export interface JdCloudSmsConfig extends SignatureSmsConfig {
  /** 地域信息 */
  region: FieldConfig;
}

/** 京东云短信配置 */
export const jdCloudSms: JdCloudSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'accessKeyId',
    required: true,
    help: '访问键标识',
  },
  accessKeySecret: {
    component: 'input',
    name: 'accessKeySecret',
    required: false,
    help: '访问键秘钥',
    type: 'password',
  },
  signature: {
    component: 'input',
    name: '默认短信签名',
    required: true,
  },
  region: {
    value: 'cn-north-1',
    component: 'input',
    name: '地域信息',
    required: true,
  },
};

export const JdCloudSmsMessageConfig = new MessageConfig(jdCloudSms, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
