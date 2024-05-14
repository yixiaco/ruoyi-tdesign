import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 租户应用管理查询对象
 */
export interface SysTenantAppQuery extends BaseEntity {
  /** 应用类型 */
  appType?: string;
  /** 应用key */
  appKey?: string;
  /** 应用名称 */
  appName?: string;
}
/**
 * 租户应用管理业务对象
 */
export interface SysTenantAppForm {
  /** 应用id */
  appid?: number;
  /** 应用类型 */
  appType?: string;
  /** 应用key */
  appKey?: string;
  /** 应用名称 */
  appName?: string;
  /** 备注 */
  remark?: string;
}
/**
 * 租户应用管理视图对象
 */
export interface SysTenantAppVo {
  /** 应用id */
  appid?: number;
  /** 应用类型 */
  appType?: string;
  /** 应用key */
  appKey?: string;
  /** 应用名称 */
  appName?: string;
  /** 创建时间 */
  createTime?: any;
  /** 更新时间 */
  updateTime?: any;
  /** 备注 */
  remark?: string;
}
