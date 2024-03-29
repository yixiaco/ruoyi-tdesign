import { alibaba } from '@/pages/system/messageConfig/data/alibabaSms';
import { cloopenSms } from '@/pages/system/messageConfig/data/cloopenSms';
import { ctyunSmsConfig } from '@/pages/system/messageConfig/data/ctyunSms';
import { dingZhongSmsConfig } from '@/pages/system/messageConfig/data/dingZhongSms';
import { emaySmsConfig } from '@/pages/system/messageConfig/data/emaySms';
import { huawei } from '@/pages/system/messageConfig/data/huaweiSms';
import { jdCloudSms } from '@/pages/system/messageConfig/data/jdCloudSms';
import { lianLuSmsConfig } from '@/pages/system/messageConfig/data/lianLuSms';
import { mail } from '@/pages/system/messageConfig/data/mail';
import { neteaseSmsConfig } from '@/pages/system/messageConfig/data/neteaseSms';
import { tencent } from '@/pages/system/messageConfig/data/tencentSms';
import { uniSms } from '@/pages/system/messageConfig/data/uniSms';
import { yunpianSms } from '@/pages/system/messageConfig/data/yunpianSms';
import { zhutongSmsConfig } from '@/pages/system/messageConfig/data/zhutongSms';
import { MessageConfig } from '@/pages/system/messageConfig/model';

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
SUPPLIER_TYPE_MAP.set('DING_ZHONG', new MessageConfig(dingZhongSmsConfig, 'SMS'));
SUPPLIER_TYPE_MAP.set('LIAN_LU', new MessageConfig(lianLuSmsConfig, 'SMS'));
