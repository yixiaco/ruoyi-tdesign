import type { FieldConfig, SmsConfig } from '../model';
import { MessageConfig } from '../model';

/** 七牛云短信 */
export interface QiNiuSmsConfig extends SmsConfig {
  /** 请求地址 */
  baseUrl: FieldConfig;
  /** 模板变量名称 */
  templateName: FieldConfig;
  /** 单发链接 */
  singleMsgUrl: FieldConfig;
  /** 群发链接 */
  massMsgUrl: FieldConfig;
  /** 签名ID */
  signatureId: FieldConfig;
}

/** 七牛云短信配置 */
export const qiNiuSmsConfig: QiNiuSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'AccessKey',
    required: true,
  },
  accessKeySecret: {
    component: 'input',
    name: 'SecretKey',
    required: true,
    type: 'password',
  },
  baseUrl: {
    component: 'input',
    name: '请求地址',
    required: true,
    value: 'https://sms.qiniuapi.com',
    help: '默认请求地址为 https://sms.qiniuapi.com',
  },
  templateName: {
    component: 'input',
    name: '模板变量名称',
    required: false,
  },
  singleMsgUrl: {
    component: 'input',
    name: '单发链接',
    required: true,
    value: '/v1/message/single',
    help: '默认单发链接为 /v1/message/single',
  },
  massMsgUrl: {
    component: 'input',
    name: '群发链接',
    required: true,
    value: '/v1/message',
    help: '默认群发链接为 /v1/message',
  },
  signatureId: {
    component: 'input',
    name: '签名ID',
    required: false,
  },
};

export const QiNiuSmsMessageConfig = new MessageConfig(qiNiuSmsConfig, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
