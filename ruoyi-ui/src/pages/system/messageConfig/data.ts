import {
  AlibabaSmsConfig,
  CloopenSmsConfig,
  CtyunSmsConfig,
  EmaySmsConfig,
  HuaweiSmsConfig,
  JdCloudSmsConfig,
  MailConfig,
  MessageConfig,
  NeteaseSmsConfig,
  TencentConfig,
  UniSmsConfig,
  YunpianSmsConfig,
  ZhutongSmsConfig,
} from './model';

const mail: MailConfig = {
  host: {
    component: 'input',
    name: 'SMTP服务器域名',
    required: true,
  },
  port: {
    value: 465,
    component: 'input-number',
    name: 'SMTP服务端口',
    required: true,
    min: 0,
    max: 65535,
  },
  auth: {
    value: false,
    component: 'switch',
    name: '用户名密码验证',
    required: true,
  },
  user: {
    component: 'input',
    name: '用户名',
    required: true,
    help: '发件人邮箱地址（如果使用foxmail邮箱，此处为qq号）',
    visible: 'auth',
  },
  pass: {
    component: 'input',
    name: '密码',
    required: true,
    help: '注意，某些邮箱需要为SMTP服务单独设置密码，详情查看相关帮助',
    visible: 'auth',
    type: 'password',
  },
  from: {
    component: 'input',
    name: '发送方',
    required: true,
    help: '邮件中显示的发件人姓名，遵循RFC-822标准。支持以下形式：1.user@xxx.xx 2.name <user@xxx.xx>',
    rules: [
      {
        pattern:
          /^([^ \f\n\r\t\v<]+@[^ \f\n\r\t\v<>]+\.[^ \f\n\r\t\v>]+$)|(\S+ <[^ \f\n\r\t\v<]+@[^ \f\n\r\t\v<>]+\.[^ \f\n\r\t\v>]+>$)/,
        message: '发送方格式错误',
        trigger: 'change',
      },
    ],
  },
  debug: {
    value: false,
    component: 'switch',
    name: '调试模式',
    required: true,
    help: '是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启',
  },
  splitlongparameters: {
    value: false,
    component: 'switch',
    name: '超长参数是否切分',
    required: true,
    help: '对于超长参数是否切分为多份，默认为false（国内邮箱附件不支持切分的附件名）',
  },
  encodefilename: {
    value: true,
    component: 'switch',
    name: '使用charset编码',
    required: true,
    help: '对于文件名是否使用charset编码，默认为 true',
  },
  starttlsEnable: {
    value: true,
    component: 'switch',
    name: 'STARTTLS安全连接',
    required: true,
    help: '使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。',
  },
  sslEnable: {
    value: true,
    component: 'switch',
    name: 'SSL安全连接',
    required: true,
  },
  sslProtocols: {
    component: 'input',
    name: 'SSL协议',
    required: false,
    help: 'SSL协议，多个协议用空格分隔',
  },
  socketFactoryClass: {
    value: 'javax.net.ssl.SSLSocketFactory',
    component: 'input',
    name: 'socketFactoryClass',
    required: true,
    help: '指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字',
  },
  socketFactoryFallback: {
    value: true,
    component: 'switch',
    name: '创建套接字工厂类',
    required: true,
    help: '如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true',
  },
  socketFactoryPort: {
    value: 465,
    component: 'input-number',
    name: 'Socket端口',
    required: true,
    min: 0,
    max: 65535,
    help: '指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口',
  },
  timeout: {
    component: 'input-number',
    name: 'SMTP超时时长',
    required: false,
    min: -1,
    help: 'SMTP超时时长，单位毫秒，缺省值不超时',
  },
  connectionTimeout: {
    component: 'input-number',
    name: 'Socket连接超时值',
    required: false,
    min: -1,
    help: 'Socket连接超时值，单位毫秒，缺省值不超时',
  },
  writeTimeout: {
    component: 'input-number',
    name: 'Socket写出超时值',
    required: false,
    min: -1,
    help: 'Socket写出超时值，单位毫秒，缺省值不超时',
  },
};
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
    type: 'password',
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
    type: 'password',
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
    type: 'password',
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
    type: 'password',
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
    type: 'password',
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
    type: 'password',
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
/** 网易云短信配置 */
const zhutongSmsConfig: ZhutongSmsConfig = {
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
    name: '短信签名',
    help: '短信签名可以为空，为空发送【自定义短信】无需要提前创建短信模板; 不为空发送:【模板短信】',
    required: false,
  },
  requestUrl: {
    value: 'https://api.mix2.zthysms.com/',
    component: 'input',
    name: '默认请求地址',
    help: '默认请求地址 不同区域，可切换请求地址，也可以不修改，请参考官方文档：https://doc.zthysms.com/web/#/1/236',
    required: true,
  },
  templateName: {
    component: 'input',
    name: '模板变量名称',
    help: '模板变量名称 查看地址：https://mix2.zthysms.com/index.html#/TemplateManagement 允许为空，为空，使用无模板形式，发送短信',
    required: false,
  },
};

export const SUPPLIER_TYPE_MAP = new Map<string, MessageConfig>();
SUPPLIER_TYPE_MAP.set('MAIL', new MessageConfig(mail, 'MAIL'));
SUPPLIER_TYPE_MAP.set('ALIBABA', new MessageConfig(alibaba, 'SMS'));
SUPPLIER_TYPE_MAP.set('TENCENT', new MessageConfig(tencent, 'SMS'));
SUPPLIER_TYPE_MAP.set('HUAWEI', new MessageConfig(huawei, 'SMS'));
SUPPLIER_TYPE_MAP.set('YUNPIAN', new MessageConfig(yunpianSms, 'SMS'));
SUPPLIER_TYPE_MAP.set('UNI_SMS', new MessageConfig(uniSms, 'SMS'));
SUPPLIER_TYPE_MAP.set('JD_CLOUD', new MessageConfig(jdCloudSms, 'SMS'));
SUPPLIER_TYPE_MAP.set('CLOOPEN', new MessageConfig(cloopenSms, 'SMS'));
SUPPLIER_TYPE_MAP.set('EMAY', new MessageConfig(emaySmsConfig, 'SMS'));
SUPPLIER_TYPE_MAP.set('CTYUN', new MessageConfig(ctyunSmsConfig, 'SMS'));
SUPPLIER_TYPE_MAP.set('NETEASE', new MessageConfig(neteaseSmsConfig, 'SMS'));
SUPPLIER_TYPE_MAP.set('ZHUTONG', new MessageConfig(zhutongSmsConfig, 'SMS'));
