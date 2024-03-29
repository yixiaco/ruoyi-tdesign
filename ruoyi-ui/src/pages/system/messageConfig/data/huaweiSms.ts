import type { FieldConfig, SignatureSmsConfig } from '../model';
import { MessageConfig } from '../model';

/** 华为云 */
export interface HuaweiSmsConfig extends SignatureSmsConfig {
  /** 国内短信签名通道号 */
  sender: FieldConfig;
  /** 短信状态报告接收地 */
  statusCallBack: FieldConfig;
  /** APP接入地址 */
  url: FieldConfig;
}

/** 华为云短信配置 */
export const huawei: HuaweiSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'appkey',
    required: true,
    help: '华为短信应用appkey',
  },
  accessKeySecret: {
    component: 'input',
    name: 'appSecret',
    required: true,
    type: 'password',
  },
  signature: {
    component: 'input',
    name: '默认短信签名',
    required: true,
  },
  sender: {
    component: 'input',
    name: '短信签名通道号',
    required: true,
  },
  statusCallBack: {
    component: 'input',
    name: '短信状态回调',
    required: false,
    help: '华为云国内短信状态报告接收地',
  },
  url: {
    value: 'https://smsapi.cn-xxxxx-4.myhuaweicloud.com:443',
    component: 'input',
    name: 'APP接入地址',
    required: true,
    help: 'APP接入地址 建立短信应用后获取到的地址',
  },
};

export const HuaweiMessageConfig = new MessageConfig(huawei, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
