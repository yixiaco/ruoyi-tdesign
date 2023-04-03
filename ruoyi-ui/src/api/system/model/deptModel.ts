import { TreeEntity } from '@/api/model/resultModel';

/**
 * 部门业务对象
 */
export interface SysDeptBo extends TreeEntity<SysDeptBo> {
  /** 部门id */
  deptId?: number;
  /** 父部门id */
  parentId?: number;
  /** 部门名称 */
  deptName?: string;
  /** 显示顺序 */
  orderNum?: number;
  /** 负责人 */
  leader?: string;
  /** 联系电话 */
  phone?: string;
  /** 邮箱 */
  email?: string;
  /** 部门状态（0正常 1停用） */
  status?: string;
}
/**
 * 部门对象 sys_dept
 */
export interface SysDept extends TreeEntity<SysDept> {
  /** 部门id */
  deptId?: number;
  /** 父部门id */
  parentId?: number;
  /** 祖级列表 */
  ancestors?: string;
  /** 部门名称 */
  deptName?: string;
  /** 显示顺序 */
  orderNum?: number;
  /** 负责人 */
  leader?: string;
  /** 联系电话 */
  phone?: string;
  /** 邮箱 */
  email?: string;
  /** 部门状态（0正常 1停用） */
  status?: string;
  /** 删除标志（0代表存在 2代表删除） */
  delFlag?: string;
  /** 创建者 */
  createBy?: number;
  /** 创建时间 */
  createTime?: any;
  /** 更新者 */
  updateBy?: number;
  /** 更新时间 */
  updateTime?: any;
}

/**
 * 部门视图对象
 */
export interface SysDeptVo {
  /** 部门id */
  deptId?: number;
  /** 父部门id */
  parentId?: number;
  /** 祖级列表 */
  ancestors?: string;
  /** 部门名称 */
  deptName?: string;
  /** 显示顺序 */
  orderNum?: number;
  /** 负责人 */
  leader?: string;
  /** 联系电话 */
  phone?: string;
  /** 邮箱 */
  email?: string;
  /** 部门状态（0正常 1停用） */
  status?: string;
  /** 创建时间 */
  createTime?: any;
}
