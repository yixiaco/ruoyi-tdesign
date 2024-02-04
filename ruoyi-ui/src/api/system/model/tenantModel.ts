import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 租户查询对象
 */
export interface SysTenantQuery extends BaseEntity {
  /** 租户编号 */
  tenantId?: string;
  /** 联系人 */
  contactUserName?: string;
  /** 联系电话 */
  contactPhone?: string;
  /** 企业名称 */
  companyName?: string;
  /** 统一社会信用代码 */
  licenseNumber?: string;
  /** 地址 */
  address?: string;
  /** 域名 */
  domain?: string;
  /** 租户套餐编号 */
  packageId?: number;
  /** 租户状态 */
  status?: string;
}
/**
 * 租户业务对象
 *
 */
export interface SysTenantForm {
  /** id */
  id?: number;
  /** 租户编号 */
  tenantId?: string;
  /** 联系人 */
  contactUserName?: string;
  /** 联系电话 */
  contactPhone?: string;
  /** 企业名称 */
  companyName?: string;
  /** 用户名（创建系统用户） */
  username?: string;
  /** 密码（创建系统用户） */
  password?: string;
  /** 统一社会信用代码 */
  licenseNumber?: string;
  /** 地址 */
  address?: string;
  /** 域名 */
  domain?: string;
  /** 企业简介 */
  intro?: string;
  /** 备注 */
  remark?: string;
  /** 租户套餐编号 */
  packageId?: number;
  /** 过期时间 */
  expireTime?: any;
  /** 用户数量（-1不限制） */
  accountCount?: number;
  /** 租户状态（1正常 0停用） */
  status?: string;
}

/**
 * 租户视图对象
 */
export interface SysTenantVo {
  /** id */
  id?: number;
  /** 租户编号 */
  tenantId?: string;
  /** 联系人 */
  contactUserName?: string;
  /** 联系电话 */
  contactPhone?: string;
  /** 企业名称 */
  companyName?: string;
  /** 统一社会信用代码 */
  licenseNumber?: string;
  /** 地址 */
  address?: string;
  /** 域名 */
  domain?: string;
  /** 企业简介 */
  intro?: string;
  /** 备注 */
  remark?: string;
  /** 租户套餐编号 */
  packageId?: number;
  /** 过期时间 */
  expireTime?: any;
  /** 用户数量（-1不限制） */
  accountCount?: number;
  /** 租户状态（1正常 0停用） */
  status?: string;
  /** 更新时间 */
  updateTime?: string;
  /** 创建时间 */
  createTime?: string;
}
