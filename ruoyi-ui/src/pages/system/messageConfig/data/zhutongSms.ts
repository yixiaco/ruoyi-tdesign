import type { FieldConfig, SignatureSmsConfig } from '../model';
import { MessageConfig } from '../model';

/** 助通短信 */
export interface ZhutongSmsConfig extends SignatureSmsConfig {
  /** 模板变量名称 查看地址：https://mix2.zthysms.com/index.html#/TemplateManagement 允许为空，为空，使用无模板形式，发送短信 */
  templateName: FieldConfig;
  /** 默认请求地址 不同区域，可切换请求地址，也可以不修改，请参考官方文档：https://doc.zthysms.com/web/#/1/236 */
  requestUrl: FieldConfig;
}

/** 助通短信配置 */
export const zhutongSmsConfig: ZhutongSmsConfig = {
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

export const ZhutongSmsMessageConfig = new MessageConfig(zhutongSmsConfig, 'SMS', {
  supportTemplateId: true,
  supportTemplateContent: true,
});
