import type { FieldConfig, SupplierConfig } from '../model';
import { MessageConfig } from '../model';

/** 联麓短信 */
export interface LianLuSmsConfig extends SupplierConfig {
  /** 企业ID */
  mchId: FieldConfig;
  /** appId */
  appId: FieldConfig;
  /** appKey */
  appKey: FieldConfig;
  /** 短信签名 */
  signature: FieldConfig;
}

/** 联麓短信配置 */
export const lianLuSmsConfig: LianLuSmsConfig = {
  mchId: {
    component: 'input',
    name: '企业ID',
    help: '请登录联麓客户端点击（通知/营销短信）进入概览页面获取',
    required: true,
  },
  appId: {
    component: 'input',
    name: 'appId',
    help: '请登录联麓客户端点击（通知/营销短信）进入概览页面获取',
    required: true,
  },
  appKey: {
    component: 'input',
    name: 'appKey',
    help: '请登录联麓客户端点击（通知/营销短信）进入概览页面获取',
    required: true,
  },
  signature: {
    component: 'input',
    name: '默认短信签名',
    help: '短信签名以实名认证公司简称或品牌名称命名。请前往联麓客户端点击（通知/营销短信）-短信签名提交审核，审核通过即可使用，未提交审核签名无法使用。例如：【联麓信息】',
    required: true,
  },
};

export const LianLuSmsMessageConfig = new MessageConfig(lianLuSmsConfig, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: false,
});
