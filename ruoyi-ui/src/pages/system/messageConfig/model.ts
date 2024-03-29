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
export interface TemplateMode {
  /** 支持模板id */
  supportTemplateId: boolean;
  /** 支持模板内容 */
  supportTemplateContent: boolean;
}
export class MessageConfig {
  /** 消息配置 */
  supplierConfig: SupplierConfig;

  /** 消息类型 */
  messageType: 'MAIL' | 'SMS';

  /** 消息模板支持类型 */
  templateMode: TemplateMode;

  constructor(supplierConfig: SupplierConfig, messageType: 'MAIL' | 'SMS', templateMode: TemplateMode) {
    this.supplierConfig = supplierConfig;
    this.messageType = messageType;
    this.templateMode = templateMode;
  }
}
/** 短信基本配置对象 */
export interface SmsConfig extends SupplierConfig {
  /** Access Key */
  accessKeyId: FieldConfig;
  /** Access Key Secret */
  accessKeySecret: FieldConfig;
}
/** 签名短信基本配置对象 */
export interface SignatureSmsConfig extends SmsConfig {
  /** 短信签名 */
  signature: FieldConfig;
}
