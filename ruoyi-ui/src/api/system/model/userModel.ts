import type { BaseEntity } from '@/api/model/resultModel';
import type { SysDeptVo } from '@/api/system/model/deptModel';
import type { SysPostVo } from '@/api/system/model/postModel';
import type { SysRoleVo } from '@/api/system/model/roleModel';

/**
 * 用户信息查询对象
 */
export interface SysUserQuery extends BaseEntity {
  /** 用户ID */
  userId?: number;
  /** 部门ID */
  deptId?: number;
  /** 用户账号 */
  userName?: string;
  /** 用户昵称 */
  nickName?: string;
  /** 用户邮箱 */
  email?: string;
  /** 手机号码 */
  phonenumber?: string;
  /** 帐号状态（1正常 0停用） */
  status?: string;
  /** 数据权限 当前角色ID */
  roleId?: number;
}
/**
 * 用户信息业务对象
 */
export interface SysUserForm {
  /** 用户ID */
  userId?: number;
  /** 部门ID */
  deptId?: number;
  /** 用户账号 */
  userName?: string;
  /** 用户昵称 */
  nickName?: string;
  /** 用户类型（sys_user系统用户） */
  userType?: string;
  /** 用户邮箱 */
  email?: string;
  /** 手机号码 */
  phonenumber?: string;
  /** 用户性别（0男 1女 2未知） */
  sex?: string;
  /** 头像地址 */
  avatar?: string;
  /** 密码 */
  password?: string;
  /** 帐号状态（1正常 0停用） */
  status?: string;
  /** 最后登录IP */
  loginIp?: string;
  /** 最后登录时间 */
  loginDate?: any;
  /** 备注 */
  remark?: string;
  /** 创建部门 */
  createDept?: number;
  /** 部门对象 */
  dept?: SysDeptVo;
  /** 角色对象 */
  roles?: Array<SysRoleVo>;
  /** 角色组 */
  roleIds?: Array<number>;
  /** 岗位组 */
  postIds?: Array<number>;
  /** 数据权限 当前角色ID */
  roleId?: number | string;
}
/**
 * 用户信息视图对象
 */
export interface SysUserVo {
  /** 用户ID */
  userId?: number;
  /** 租户ID */
  tenantId?: string;
  /** 租户企业名称 */
  tenantName?: string;
  /** 部门ID */
  deptId?: number;
  /** 用户账号 */
  userName?: string;
  /** 用户昵称 */
  nickName?: string;
  /** 用户类型（sys_user系统用户） */
  userType?: string;
  /** 用户邮箱 */
  email?: string;
  /** 手机号码 */
  phonenumber?: string;
  /** 用户性别（0男 1女 2未知） */
  sex?: string;
  /** 头像地址 */
  avatar?: string;
  /** 密码 */
  password?: string;
  /** 帐号状态（1正常 0停用） */
  status?: string;
  /** 最后登录IP */
  loginIp?: string;
  /** 最后登录时间 */
  loginDate?: any;
  /** 创建时间 */
  createTime?: any;
  /** 更新时间 */
  updateTime?: any;
  /** 备注 */
  remark?: string;
  /** 部门对象 */
  dept?: SysDeptVo;
  /** 角色对象 */
  roles?: SysRoleVo[];
  /** 角色组 */
  roleIds?: number[];
  /** 岗位组 */
  postIds?: number[];
  /** 数据权限 当前角色ID */
  roleId?: number;
}

/**
 * 个人信息业务处理
 */
export interface SysUserProfileBo {
  /** 用户昵称 */
  nickName?: string;
  /** 用户性别（0男 1女 2未知） */
  sex?: string;
}

export interface ProfileVo {
  /** 用户信息 */
  user?: SysUserVo;
  /** 用户所属角色组 */
  roleGroup?: string;
  /** 用户所属岗位组 */
  postGroup?: string;
  /** 部门 */
  deptGroup?: string;
}

export interface AvatarVo {
  imgUrl?: string;
}

export interface UserAuthRole {
  user?: SysUserVo;
  roles?: Array<SysRoleVo>;
}

export interface SysUserInfoVo {
  roles?: Array<SysRoleVo>;
  posts?: Array<SysPostVo>;
  user?: SysUserVo;
  postIds?: Array<number>;
  roleIds?: Array<number>;
}
