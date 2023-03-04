import { TreeEntity } from '@/api/model/ResultModel';

// 系统部门
export interface SysDept extends TreeEntity<SysDept> {
  /**
   * 部门ID
   */
  deptId?: number;

  /**
   * 部门名称
   */
  deptName?: string;

  /**
   * 上级部门id
   */
  parentId?: number;

  /**
   * 显示顺序
   */
  orderNum?: number;

  /**
   * 负责人
   */
  leader?: string;

  /**
   * 联系电话
   */
  phone?: string;

  /**
   * 邮箱
   */
  email?: string;

  /**
   * 部门状态?:0正常,1停用
   */
  status?: string;

  /**
   * 删除标志（0代表存在 2代表删除）
   */
  delFlag?: string;

  /**
   * 祖级列表
   */
  ancestors?: string;

  /**
   * 创建者
   */
  createBy?: string;

  /**
   * 创建时间
   */
  createTime?: object;

  /**
   * 更新者
   */
  updateBy?: string;

  /**
   * 更新时间
   */
  updateTime?: object;
}
