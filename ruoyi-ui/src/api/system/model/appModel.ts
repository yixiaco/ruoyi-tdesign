import { BaseEntity } from '@/api/model/resultModel';

/**
 * 应用管理查询对象
 */
export interface SysAppQuery extends BaseEntity {
  /** 应用类型 */
  appType?: string;
  /** 应用key */
  appKey?: string;
  /** 应用名称 */
  appName?: string;
}
/**
 * 应用管理业务对象
 */
export interface SysAppForm extends BaseEntity {
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
 * 应用管理视图对象
 */
export interface SysAppVo {
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
