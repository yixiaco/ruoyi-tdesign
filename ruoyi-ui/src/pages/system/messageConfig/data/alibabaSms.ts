import type { FieldConfig, SignatureSmsConfig } from '../model';
import { MessageConfig } from '../model';

/** 阿里云短信 */
export interface AlibabaSmsConfig extends SignatureSmsConfig {
  /** 请求地址 */
  requestUrl: FieldConfig;
  /** 接口名称 */
  action: FieldConfig;
  /** 地域信息默认为 cn-hangzhou */
  regionId: FieldConfig;
  /** 接口版本号 */
  version: FieldConfig;
}

/** 阿里云短信配置 */
export const alibaba: AlibabaSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'accessKey',
    help: '如何获取AccessKey，请查询: (https://help.aliyun.com/document_detail/116401.htm) 或<br /> (https://usercenter.console.aliyun.com/#/manage/ak)',
    required: true,
  },
  accessKeySecret: {
    component: 'input',
    name: 'accessKeySecret',
    help: '阿里云的accessKeySecret',
    required: true,
    type: 'password',
  },
  signature: {
    component: 'input',
    name: '默认短信签名',
    required: true,
    help: '签名信息可前往 https://dysms.console.aliyun.com/domestic/text/sign 的签名管理查看',
  },
  action: {
    value: 'SendSms',
    component: 'input',
    name: '接口方法',
    required: true,
    help: '接口方法默认为 SendSms 如无特殊改变可以不用设置',
  },
  regionId: {
    value: 'cn-hangzhou',
    component: 'input',
    name: '地域',
    required: true,
    help: '地域信息默认为 cn-hangzhou 如无特殊改变可以不用设置',
  },
  requestUrl: {
    value: 'dysmsapi.aliyuncs.com',
    component: 'input',
    name: '请求地址',
    required: true,
    help: '请求地址默认为 dysmsapi.aliyuncs.com 如无特殊改变可以不用设置',
  },
  version: {
    value: '2017-05-25',
    component: 'input',
    name: '接口版本号',
    required: true,
    help: '接口版本号默认为 2017-05-25 如无特殊改变可以不用设置',
  },
};

export const AlibabaMessageConfig = new MessageConfig(alibaba, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
