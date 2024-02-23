import type { EmaySmsConfig } from '@/pages/system/messageConfig/model';

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
