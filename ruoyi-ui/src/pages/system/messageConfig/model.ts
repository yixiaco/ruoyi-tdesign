/** 字段基本配置对象 */
export interface FieldConfig {
  /** 字段默认值 */
  value?: string | number | boolean;
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
export type SupplierConfig = Record<string, FieldConfig>;
/** 短信基本配置对象 */
export interface SmsConfig extends SupplierConfig {
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
/** 华为云 */
export interface HuaweiSmsConfig extends SupplierConfig {
  /** appKey */
  appKey: FieldConfig;
  /** appSecret */
  appSecret: FieldConfig;
  /** 短信签名 */
  signature: FieldConfig;
  /** 国内短信签名通道号 */
  sender: FieldConfig;
  /** 短信状态报告接收地 */
  statusCallBack: FieldConfig;
  /** APP接入地址 */
  url: FieldConfig;
}
/** 合一短信 */
export interface UniSmsConfig extends SmsConfig {
  /** 是否为简易模式 */
  isSimple: FieldConfig;
}
/** 云片短信 */
export interface YunpianSmsConfig extends SupplierConfig {
  /** Access Key */
  accessKeyId: FieldConfig;
  /** 短信签名 */
  signature: FieldConfig;
  /** 短信发送后将向这个地址推送(运营商返回的)发送报告 */
  callbackUrl: FieldConfig;
}
/** 京东云短信 */
export interface JdCloudSmsConfig extends SmsConfig {
  /** 地域信息 */
  region: FieldConfig;
}
/** 容联云短信 */
export interface CloopenSmsConfig extends SupplierConfig {
  /** Access Key */
  accessKeyId: FieldConfig;
  /** Access Key Secret */
  accessKeySecret: FieldConfig;
  /** 应用 ID */
  appId: FieldConfig;
  /** REST API Base URL */
  baseUrl: FieldConfig;
}
/** 亿美软通 */
export interface EmaySmsConfig extends SupplierConfig {
  /** appKey */
  appId: FieldConfig;
  /** appSecret */
  secretKey: FieldConfig;
  /** APP接入地址 */
  requestUrl: FieldConfig;
}
/** 天翼云短信 */
export interface CtyunSmsConfig extends SmsConfig {
  /** 请求地址 */
  requestUrl: FieldConfig;
  /** 接口名称 */
  action: FieldConfig;
}
/** 网易云短信 */
export interface NeteaseSmsConfig extends SmsConfig {
  /** 模板短信请求地址 */
  templateUrl: FieldConfig;
  /** 验证码短信请求地址 */
  codeUrl: FieldConfig;
  /** 验证码验证请求地址 */
  verifyUrl: FieldConfig;
  /** 是否需要支持短信上行。true:需要，false:不需要 说明：如果开通了短信上行抄送功能，该参数需要设置为true，其它情况设置无效 */
  needUp: FieldConfig;
}
