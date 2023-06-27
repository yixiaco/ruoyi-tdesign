import {
  AlibabaSmsConfig,
  CloopenSmsConfig,
  CtyunSmsConfig,
  EmaySmsConfig,
  HuaweiSmsConfig,
  JdCloudSmsConfig,
  NeteaseSmsConfig,
  SupplierConfig,
  TencentConfig,
  UniSmsConfig,
  YunpianSmsConfig,
} from './model';

/** 阿里云短信配置 */
const alibaba: AlibabaSmsConfig = {
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

/** 腾讯云短信配置 */
const tencent: TencentConfig = {
  accessKeyId: {
    component: 'input',
    name: 'accessKey',
    help: '腾讯云的accessKey。SecretId、SecretKey 查询: https://console.cloud.tencent.com/cam/capi',
    required: true,
  },
  accessKeySecret: {
    component: 'input',
    name: 'accessKeySecret',
    help: '腾讯云的accessKeySecret',
    required: true,
  },
  signature: {
    component: 'input',
    name: '默认短信签名',
    required: true,
    help:
      '签名信息可前往 [国内短信](https://console.cloud.tencent.com/smsv2/csms-sign) 或<br />' +
      '[国际/港澳台短信](https://console.cloud.tencent.com/smsv2/isms-sign) 的签名管理查看',
  },
  sdkAppId: {
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
    name: '接口方法',
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
/** 华为云短信配置 */
const huawei: HuaweiSmsConfig = {
  appKey: {
    component: 'input',
    name: 'appkey',
    required: true,
    help: '华为短信应用appkey',
  },
  appSecret: {
    component: 'input',
    name: 'appSecret',
    required: true,
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
/** 合一短信配置 */
const uniSms: UniSmsConfig = {
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
    help: '访问键秘钥 简易模式不需要配置',
  },
  signature: {
    component: 'input',
    name: '默认短信签名',
    required: true,
  },
  isSimple: {
    value: true,
    component: 'switch',
    name: '简易模式',
    required: true,
    help: '是否为简易模式 默认为true',
  },
};
/** 云片短信配置 */
const yunpianSms: YunpianSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'apikey',
    help: '账号唯一标识',
    required: true,
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
/** 京东云短信配置 */
const jdCloudSms: JdCloudSmsConfig = {
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
/** 容联云短信配置 */
const cloopenSms: CloopenSmsConfig = {
  accessKeyId: {
    component: 'input',
    name: 'accessKeyId',
    required: true,
    help: '访问键标识',
  },
  accessKeySecret: {
    component: 'input',
    name: 'accessKeySecret',
    required: true,
    help: '访问键秘钥',
  },
  appId: {
    component: 'input',
    name: '应用 ID',
    required: true,
  },
  baseUrl: {
    value: 'https://app.cloopen.com:8883/2013-12-26',
    component: 'input',
    name: 'BaseURL',
    required: true,
  },
};
/** 亿美软通短信配置 */
const emaySmsConfig: EmaySmsConfig = {
  appId: {
    component: 'input',
    name: 'appId',
    required: true,
    help: '访问键标识',
  },
  secretKey: {
    component: 'input',
    name: 'secretKey',
    required: true,
    help: '访问键秘钥',
  },
  requestUrl: {
    component: 'input',
    name: '请求地址',
    required: true,
    help: '短信发送请求地址',
  },
};
/** 天翼云短信配置 */
const ctyunSmsConfig: CtyunSmsConfig = {
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
/** 网易云短信配置 */
const neteaseSmsConfig: NeteaseSmsConfig = {
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

export const SUPPLIER_TYPE_MAP = new Map<string, SupplierConfig>();
SUPPLIER_TYPE_MAP.set('ALIBABA', alibaba);
SUPPLIER_TYPE_MAP.set('TENCENT', tencent);
SUPPLIER_TYPE_MAP.set('HUAWEI', huawei);
SUPPLIER_TYPE_MAP.set('YUNPIAN', yunpianSms);
SUPPLIER_TYPE_MAP.set('UNI_SMS', uniSms);
SUPPLIER_TYPE_MAP.set('JD_CLOUD', jdCloudSms);
SUPPLIER_TYPE_MAP.set('CLOOPEN', cloopenSms);
SUPPLIER_TYPE_MAP.set('EMAY', emaySmsConfig);
SUPPLIER_TYPE_MAP.set('CTYUN', ctyunSmsConfig);
SUPPLIER_TYPE_MAP.set('NETEASE', neteaseSmsConfig);
SUPPLIER_TYPE_MAP.set('MAIL', null);
