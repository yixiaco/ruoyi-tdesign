/**
 * 参数配置业务对象
 */
export interface SysConfigForm {
  /** 参数主键 */
  configId?: number;
  /** 参数名称 */
  configName?: string;
  /** 参数键名 */
  configKey?: string;
  /** 参数键值 */
  configValue?: string;
  /** 系统内置（Y是 N否） */
  configType?: string;
  /** 是否是全局配置 1是 0否 */
  isGlobal?: number;
  /** 备注 */
  remark?: string;
}
/**
 * 参数配置视图对象
 */
export interface SysConfigVo {
  /** 参数主键 */
  configId?: number;
  /** 参数名称 */
  configName?: string;
  /** 参数键名 */
  configKey?: string;
  /** 参数键值 */
  configValue?: string;
  /** 系统内置（Y是 N否） */
  configType?: string;
  /** 是否是全局配置 1是 0否 */
  isGlobal?: number;
  /** 创建时间 */
  createTime?: any;
  /** 备注 */
  remark?: string;
}
