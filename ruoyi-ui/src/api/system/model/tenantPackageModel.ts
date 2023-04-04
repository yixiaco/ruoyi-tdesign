/**
 * 租户套餐业务对象 sys_tenant_package
 *
 */
import { BaseEntity } from '@/api/model/resultModel';

export interface SysTenantPackageBo extends BaseEntity {
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
  /** 状态（0正常 1停用） */
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
  /** 关联菜单id */
  menuIds?: string;
  /** 备注 */
  remark?: string;
  /** 菜单树选择项是否关联显示 */
  menuCheckStrictly?: boolean;
  /** 状态（0正常 1停用） */
  status?: string;
}
