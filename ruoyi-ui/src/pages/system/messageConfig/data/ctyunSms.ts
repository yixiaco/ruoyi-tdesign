import type { FieldConfig, SignatureSmsConfig } from '../model';
import { MessageConfig } from '../model';

/** 天翼云短信 */
export interface CtyunSmsConfig extends SignatureSmsConfig {
  /** 请求地址 */
  requestUrl: FieldConfig;
  /** 接口名称 */
  action: FieldConfig;
}

/** 天翼云短信配置 */
export const ctyunSmsConfig: CtyunSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'accessKey',
    help: '天翼云的accessKey',
    required: true,
  },
  accessKeySecret: {
    component: 'input',
    name: 'accessKeySecret',
    help: '天翼云的accessKeySecret',
    required: true,
    type: 'password',
  },
  signature: {
    component: 'input',
    name: '默认短信签名',
    required: true,
  },
  requestUrl: {
    value: 'https://sms-global.ctapi.ctyun.cn/sms/api/v1',
    component: 'input',
    name: '请求地址',
    required: true,
    help: '请求地址默认为 https://sms-global.ctapi.ctyun.cn/sms/api/v1 如无特殊改变可以不用设置',
  },
  action: {
    value: 'SendSms',
    component: 'input',
    name: '接口方法',
    required: true,
    help: '接口方法默认为 SendSms 如无特殊改变可以不用设置',
  },
};

export const CtyunSmsMessageConfig = new MessageConfig(ctyunSmsConfig, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
