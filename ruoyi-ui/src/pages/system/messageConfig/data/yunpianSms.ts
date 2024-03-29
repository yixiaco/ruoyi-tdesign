import type { FieldConfig, SupplierConfig } from '../model';
import { MessageConfig } from '../model';

/** 云片短信 */
export interface YunpianSmsConfig extends SupplierConfig {
  /** Access Key */
  accessKeyId: FieldConfig;
  /** 短信签名 */
  signature: FieldConfig;
  /** 短信发送后将向这个地址推送(运营商返回的)发送报告 */
  callbackUrl: FieldConfig;
}

/** 云片短信配置 */
export const yunpianSms: YunpianSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'apikey',
    help: '账号唯一标识',
    required: true,
    type: 'password',
  },
  signature: {
    component: 'input',
    name: '默认短信签名',
    required: false,
  },
  callbackUrl: {
    component: 'input',
    name: '回调地址',
    required: false,
    help: '云片官方回调地址，无需求可以不设置',
  },
};

export const YunpianSmsMessageConfig = new MessageConfig(yunpianSms, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
