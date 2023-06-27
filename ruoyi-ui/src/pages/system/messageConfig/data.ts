/** 字段基本配置对象 */
export interface FieldConfig {
  /** 字段默认值 */
  value?: string | number;
  /** 字段名称 */
  name: string;
  /** 组件 */
  component: string;
  /** 帮助信息 */
  help?: string;
  /** 是否必填 */
  required: boolean;
  /** 选项 */
  options?: [];
  /** 占用栅格数 */
  span?: number;
  /** 最小值 */
  min?: number;
  /** 最大值 */
  max?: number;
}
/** 短信基本配置对象 */
export interface SmsConfig {
  /** Access Key */
  accessKeyId: FieldConfig;
  /** Access Key Secret */
  accessKeySecret: FieldConfig;
  /** 短信签名 */
  signature: FieldConfig;
}
/** 阿里云短信 */
export interface AlibabaSmsConfig extends SmsConfig {
  /** 请求地址 */
  requestUrl: FieldConfig;
  /** 接口名称 */
  action: FieldConfig;
  /** 地域信息默认为 cn-hangzhou */
  regionId: FieldConfig;
  /** 接口版本号 */
  version: FieldConfig;
}
/** 腾讯云短信 */
export interface TencentConfig extends SmsConfig {
  /** 短信sdkAppId */
  sdkAppId: FieldConfig;
  /** 地域信息默认为 ap-guangzhou */
  territory: FieldConfig;
  /** 请求超时时间 */
  connTimeout: FieldConfig;
  /** 请求地址 */
  requestUrl: FieldConfig;
  /** 接口名称 */
  action: FieldConfig;
  /** 接口版本 */
  version: FieldConfig;
}

/** 阿里云短信配置 */
const alibaba: AlibabaSmsConfig = {
  accessKeyId: {
    value: '',
    component: 'input',
    name: 'accessKey',
    help: '如何获取AccessKey，请查询: (https://help.aliyun.com/document_detail/116401.htm) 或<br /> (https://usercenter.console.aliyun.com/#/manage/ak)',
    required: true,
  },
  accessKeySecret: {
    value: '',
    component: 'input',
    name: 'accessKeySecret',
    help: '阿里云的accessKeySecret',
    required: true,
  },
  signature: {
    value: '',
    component: 'input',
    name: '默认短信签名',
    required: true,
    help: '签名信息可前往 https://dysms.console.aliyun.com/domestic/text/sign 的签名管理查看',
  },
  action: {
    value: 'SendSms',
    component: 'input',
    name: '请求地址',
    required: true,
    help: '接口方法默认为 SendSms 如无特殊改变可以不用设置',
  },
  regionId: {
    value: 'cn-hangzhou',
    component: 'input',
    name: '短信签名',
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

/** 腾讯云短信配置 */
const tencent: TencentConfig = {
  accessKeyId: {
    value: '',
    component: 'input',
    name: 'accessKey',
    help: '腾讯云的accessKey。SecretId、SecretKey 查询: https://console.cloud.tencent.com/cam/capi',
    required: true,
  },
  accessKeySecret: {
    value: '',
    component: 'input',
    name: 'accessKeySecret',
    help: '腾讯云的accessKeySecret',
    required: true,
  },
  signature: {
    value: '',
    component: 'input',
    name: '默认短信签名',
    required: true,
    help:
      '签名信息可前往 [国内短信](https://console.cloud.tencent.com/smsv2/csms-sign) 或<br />' +
      '[国际/港澳台短信](https://console.cloud.tencent.com/smsv2/isms-sign) 的签名管理查看',
  },
  sdkAppId: {
    value: '',
    component: 'input',
    name: '短信应用ID',
    help: '短信sdkAppId。应用 ID 可前往 [短信控制台](https://console.cloud.tencent.com/smsv2/app-manage) 查看',
    required: true,
  },
  territory: {
    value: 'ap-guangzhou',
    component: 'input',
    name: '地域',
    help: '地域信息默认为 ap-guangzhou 如无特殊改变可不用设置',
    required: true,
  },
  connTimeout: {
    value: 60,
    component: 'input-number',
    name: '请求超时时间',
    help: '请求超时时间 默认60秒',
    required: true,
    min: 30,
    max: 180,
  },
  requestUrl: {
    value: 'sms.tencentcloudapi.com',
    component: 'input',
    name: '请求地址',
    help: '请求地址默认为 sms.tencentcloudapi.com 如无特殊改变可不用设置',
    required: true,
  },
  action: {
    value: 'SendSms',
    component: 'input',
    name: '请求地址',
    required: true,
    help: '接口方法默认为 SendSms 如无特殊改变可以不用设置',
  },
  version: {
    value: '2021-01-11',
    component: 'input',
    name: '接口版本号',
    required: true,
    help: '接口版本默认为 2021-01-11 如无特殊改变可不用设置',
  },
};

export const SUPPLIER_TYPE_MAP = new Map<string, SmsConfig>();
SUPPLIER_TYPE_MAP.set('ALIBABA', alibaba);
SUPPLIER_TYPE_MAP.set('TENCENT', tencent);
