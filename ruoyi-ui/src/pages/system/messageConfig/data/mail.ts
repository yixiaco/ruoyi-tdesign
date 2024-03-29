import type { FieldConfig, SupplierConfig } from '../model';
import { MessageConfig } from '../model';

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

export const mail: MailConfig = {
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

export const MailMessageConfig = new MessageConfig(mail, 'MAIL', {
  supportTemplateId: false,
  supportTemplateContent: true,
});
