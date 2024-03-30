import type { FieldConfig, SignatureSmsConfig } from '../model';
import { MessageConfig } from '../model';

/** 网易云短信 */
export interface NeteaseSmsConfig extends SignatureSmsConfig {
  /** 模板短信请求地址 */
  templateUrl: FieldConfig;
  /** 验证码短信请求地址 */
  codeUrl: FieldConfig;
  /** 验证码验证请求地址 */
  verifyUrl: FieldConfig;
  /** 是否需要支持短信上行。true:需要，false:不需要 说明：如果开通了短信上行抄送功能，该参数需要设置为true，其它情况设置无效 */
  needUp: FieldConfig<boolean>;
}

/** 网易云短信配置 */
export const neteaseSmsConfig: NeteaseSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'accessKey',
    help: '访问键标识',
    required: true,
  },
  accessKeySecret: {
    component: 'input',
    name: 'accessKeySecret',
    help: '访问键秘钥 ',
    required: true,
    type: 'password',
  },
  signature: {
    component: 'input',
    name: '默认短信签名',
    required: true,
  },
  templateUrl: {
    value: 'https://api.netease.im/sms/sendtemplate.action',
    component: 'input',
    name: '模板短信请求地址',
    required: true,
  },
  codeUrl: {
    value: 'https://api.netease.im/sms/sendcode.action',
    component: 'input',
    name: '验证码短信请求地址',
    required: true,
  },
  verifyUrl: {
    value: 'https://api.netease.im/sms/verifycode.action',
    component: 'input',
    name: '验证码验证请求地址',
    required: true,
  },
  needUp: {
    value: false,
    component: 'switch',
    name: '支持短信上行',
    required: true,
    help: '是否需要支持短信上行。true:需要，false:不需要 说明：如果开通了短信上行抄送功能，该参数需要设置为true，其它情况设置无效\n',
  },
};

export const NeteaseSmsMessageConfig = new MessageConfig(neteaseSmsConfig, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
