import { BaseEntity } from '@/api/model/resultModel';

/**
 * 岗位信息业务对象
 */
export interface SysPostBo extends BaseEntity {
  /** 岗位ID */
  postId?: number;
  /** 岗位编码 */
  postCode?: string;
  /** 岗位名称 */
  postName?: string;
  /** 显示顺序 */
  postSort?: number;
  /** 状态（0正常 1停用） */
  status?: string;
  /** 备注 */
  remark?: string;
}
/**
 * 岗位
 */
export interface SysPost extends BaseEntity {
  /** 岗位序号 */
  postId?: number;
  /** 岗位编码 */
  postCode?: string;
  /** 岗位名称 */
  postName?: string;
  /** 岗位排序 */
  postSort?: number;
  /** 状态（0正常 1停用） */
  status?: string;
  /** 备注 */
  remark?: string;
  /** 用户是否存在此岗位标识 默认不存在 */
  flag?: boolean;
  /** 创建者 */
  createBy?: string;
  /** 创建时间 */
  createTime?: object;
  /** 更新者 */
  updateBy?: string;
  /** 更新时间 */
  updateTime?: object;
}
/**
 * 岗位信息视图对象
 */
export interface SysPostVo {
  /** 岗位ID */
  postId?: number;
  /** 岗位编码 */
  postCode?: string;
  /** 岗位名称 */
  postName?: string;
  /** 显示顺序 */
  postSort?: number;
  /** 状态（0正常 1停用） */
  status?: string;
  /** 创建时间 */
  createTime?: any;
  /** 备注 */
  remark?: string;
}
