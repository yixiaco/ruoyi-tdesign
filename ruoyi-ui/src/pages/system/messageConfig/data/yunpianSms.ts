import type { YunpianSmsConfig } from '@/pages/system/messageConfig/model';

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
