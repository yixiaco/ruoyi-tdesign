import { BaseEntity } from '@/api/model/resultModel';

/**
 * 部门业务对象
 */
export interface SysDeptBo extends BaseEntity {
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
