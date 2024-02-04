import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 系统授权查询对象
 */
export interface SysClientQuery extends BaseEntity {
  /** 客户端id */
  clientId?: string;
  /** 客户端key */
  clientKey?: string;
  /** 状态（1正常 0停用） */
  status?: string;
}
/**
 * 系统授权业务对象
 */
export interface SysClientForm {
  /** id */
  id?: number;
  /** 客户端key */
  clientKey?: string;
  /** 客户端秘钥 */
  clientSecret?: string;
  /** 授权类型 */
  grantType?: string;
  /** 授权类型 */
  grantTypeList?: string[];
  /** 设备类型 */
  deviceType?: string;
  /** token活跃超时时间 */
  activeTimeout?: number;
  /** token固定超时 */
  timeout?: number;
  /** 状态（1正常 0停用） */
  status?: string;
}
/**
 * 系统授权视图对象
 */
export interface SysClientVo {
  /** id */
  id?: number;
  /** 客户端id */
  clientId?: string;
  /** 客户端key */
  clientKey?: string;
  /** 客户端秘钥 */
  clientSecret?: string;
  /** 授权类型 */
  grantType?: string;
  /** 设备类型 */
  deviceType?: string;
  /** token活跃超时时间 */
  activeTimeout?: number;
  /** token固定超时 */
  timeout?: number;
  /** 状态（1正常 0停用） */
  status?: string;
  /** 创建时间 */
  createTime?: any;
  /** 更新时间 */
  updateTime?: any;
}
