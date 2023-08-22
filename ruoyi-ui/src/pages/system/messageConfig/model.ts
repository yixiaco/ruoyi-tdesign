import type { FormRule } from 'tdesign-vue-next';

/** 字段基本配置对象 */
export interface FieldConfig<T extends string | number | boolean | Array<string | number> = string> {
  /** 字段默认值 */
  value?: T;
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
  /** 可见性依赖字段。例如a=true，则b设置visible为a */
  visible?: string;
  /** 其他校验规则 */
  rules?: FormRule[];
  /** 组件类型 */
  type?: 'number' | 'submit' | 'url' | 'text' | 'search' | 'password' | 'hidden' | 'tel';
}
export type SupplierConfig = Record<string, FieldConfig<any>>;
export class MessageConfig {
  supplierConfig: SupplierConfig;

  messageType: 'MAIL' | 'SMS';

  constructor(supplierConfig: SupplierConfig, messageType: 'MAIL' | 'SMS') {
    this.supplierConfig = supplierConfig;
    this.messageType = messageType;
  }
}
/** 短信基本配置对象 */
export interface SmsConfig extends SupplierConfig {
  /** Access Key */
  accessKeyId: FieldConfig;
  /** Access Key Secret */
  accessKeySecret: FieldConfig;
  /** 短信签名 */
  signature: FieldConfig;
}
/** 邮箱配置对象 */
export interface MailConfig extends SupplierConfig {
  /** SMTP服务器域名 */
  host: FieldConfig;
  /** SMTP服务端口 */
  port: FieldConfig<number>;
  /** 是否需要用户名密码验证 */
  auth: FieldConfig<boolean>;
  /** 用户名 */
  user: FieldConfig;
  /** 密码 */
  pass: FieldConfig;
  /** 发送方，遵循RFC-822标准 */
  from: FieldConfig;
  /** 是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启 */
  debug: FieldConfig<boolean>;
  /** 对于超长参数是否切分为多份，默认为false（国内邮箱附件不支持切分的附件名） */
  splitlongparameters: FieldConfig<boolean>;
  /** 对于文件名是否使用{@link #charset}编码，默认为true */
  encodefilename: FieldConfig<boolean>;
  /** 使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。 */
  starttlsEnable: FieldConfig<boolean>;
  /** 使用 SSL安全连接 */
  sslEnable: FieldConfig<boolean>;
  /** SSL协议，多个协议用空格分隔 */
  sslProtocols: FieldConfig;
  /** 指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字 */
  socketFactoryClass: FieldConfig;
  /** 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true */
  socketFactoryFallback: FieldConfig<boolean>;
  /** 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口 */
  socketFactoryPort: FieldConfig<number>;
  /** SMTP超时时长，单位毫秒，缺省值不超时 */
  timeout: FieldConfig<number>;
  /** Socket连接超时值，单位毫秒，缺省值不超时 */
  connectionTimeout: FieldConfig<number>;
  /** Socket写出超时值，单位毫秒，缺省值不超时 */
  writeTimeout: FieldConfig<number>;
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
  connTimeout: FieldConfig<number>;
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
  isSimple: FieldConfig<boolean>;
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
  needUp: FieldConfig<boolean>;
}
