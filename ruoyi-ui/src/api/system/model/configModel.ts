import { BaseEntity } from '@/api/model/resultModel';

/**
 * 参数配置
 */
export interface SysConfig extends BaseEntity {
  /**
   * 参数主键
   */
  configId: number;

  /**
   * 参数名称
   */
  configName: string;

  /**
   * 参数键名
   */
  configKey: string;

  /**
   * 参数键值
   */
  configValue: string;

  /**
   * 系统内置（Y是 N否）
   */
  configType: string;

  /**
   * 备注
   */
  remark: string;
}
