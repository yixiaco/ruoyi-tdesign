/**
 * 租户套餐业务对象 sys_tenant_package
 *
 */
import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 租户套餐查询对象
 */
export interface SysTenantPackageQuery extends BaseEntity {
  /** 套餐名称 */
  packageName?: string;
  /** 状态（1正常 0停用） */
  status?: string;
}
export interface SysTenantPackageForm {
  /** 租户套餐id */
  packageId?: number;
  /** 套餐名称 */
  packageName?: string;
  /** 关联菜单id */
  menuIds?: number[];
  /** 备注 */
  remark?: string;
  /** 菜单树选择项是否关联显示 */
  menuCheckStrictly?: boolean;
  /** 状态（1正常 0停用） */
  status?: string;
}
/**
 * 租户套餐视图对象 sys_tenant_package
 *
 */
export interface SysTenantPackageVo {
  /** 租户套餐id */
  packageId?: number;
  /** 套餐名称 */
  packageName?: string;
  /** 备注 */
  remark?: string;
  /** 菜单树选择项是否关联显示 */
  menuCheckStrictly?: boolean;
  /** 状态（1正常 0停用） */
  status?: string;
  /** 创建时间 */
  createTime?: any;
  /** 更新时间 */
  updateTime?: any;
}
