import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 系统访问记录查询对象
 */
export interface SysLogininforQuery extends BaseEntity {
  /** 用户id */
  userId?: number;
  /** 用户账号 */
  userName?: string;
  /** 登录IP地址 */
  ipaddr?: string;
  /** 登录状态（1成功 0失败） */
  status?: string;
  /** 客户端 */
  clientKey?: string;
  /** 设备类型 */
  deviceType?: string;
}
/**
 * 系统访问记录视图对象
 */
export interface SysLogininforVo {
  /** 访问ID */
  infoId?: number;
  /** 租户编号 */
  tenantId?: string;
  /** 用户id */
  userId?: number;
  /** 用户账号 */
  userName?: string;
  /** 登录IP地址 */
  ipaddr?: string;
  /** 登录地点 */
  loginLocation?: string;
  /** 浏览器类型 */
  browser?: string;
  /** 操作系统 */
  os?: string;
  /** 登录状态（1成功 0失败） */
  status?: string;
  /** 客户端 */
  clientKey?: string;
  /** 设备类型 */
  deviceType?: string;
  /** 提示消息 */
  msg?: string;
  /** 访问时间 */
  loginTime?: any;
}
