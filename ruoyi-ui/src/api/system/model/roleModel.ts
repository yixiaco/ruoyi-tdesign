import type { BaseEntity, TreeModel } from '@/api/model/resultModel';

/**
 * 角色信息查询对象
 */
export interface SysRoleQuery extends BaseEntity {
  /** 角色ID */
  roleId?: number;
  /** 角色名称 */
  roleName?: string;
  /** 角色权限字符串 */
  roleKey?: string;
  /** 角色状态（1正常 0停用） */
  status?: string;
  /** 创建时间 */
  createTime?: any;
}
/**
 * 角色信息业务对象
 */
export interface SysRoleForm {
  /** 角色ID */
  roleId?: number;
  /** 角色名称 */
  roleName?: string;
  /** 角色权限字符串 */
  roleKey?: string;
  /** 显示顺序 */
  roleSort?: number;
  /** 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限） */
  dataScope?: string;
  /** 菜单树选择项是否关联显示 */
  menuCheckStrictly?: boolean;
  /** 部门树选择项是否关联显示 */
  deptCheckStrictly?: boolean;
  /** 角色状态（1正常 0停用） */
  status?: string;
  /** 备注 */
  remark?: string;
  /** 菜单组 */
  menuIds?: Array<number>;
  /** 部门组（数据权限） */
  deptIds?: Array<number>;
  /** 角色菜单权限 */
  permissions?: Array<string>;
}
/**
 * 角色信息视图对象
 */
export interface SysRoleVo {
  /** 角色ID */
  roleId?: number;
  /** 角色名称 */
  roleName?: string;
  /** 角色权限字符串 */
  roleKey?: string;
  /** 显示顺序 */
  roleSort?: number;
  /** 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限） */
  dataScope?: string;
  /** 菜单树选择项是否关联显示 */
  menuCheckStrictly?: boolean;
  /** 部门树选择项是否关联显示 */
  deptCheckStrictly?: boolean;
  /** 角色状态（1正常 0停用） */
  status?: string;
  /** 创建时间 */
  createTime?: any;
  /** 更新时间 */
  updateTime?: any;
  /** 备注 */
  remark?: string;
  /** 用户是否存在此角色标识 默认不存在 */
  flag?: boolean;
  /** 菜单组 */
  menuIds?: Array<number>;
  /** 部门组（数据权限） */
  deptIds?: Array<number>;
}

/**
 * 角色部门树列表
 */
export interface DeptTreeSelect {
  checkedKeys?: Array<number>;
  depts?: Array<TreeModel<number>>;
}

export interface SysUserRole {
  /** 用户ID */
  userId?: number;
  /** 角色ID */
  roleId?: number | string;
}
