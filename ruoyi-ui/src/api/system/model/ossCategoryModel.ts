import { BaseEntity } from '@/api/model/resultModel';

/**
 * OSS分类查询对象
 */
export interface SysOssCategoryQuery extends BaseEntity {
  /** 分类名称 */
  categoryName?: string;
  /** 用户类型 */
  userType?: string;
  /** 上传人 */
  createBy?: number;
}
/**
 * OSS分类业务对象
 */
export interface SysOssCategoryForm extends BaseEntity {
  /** oss分类id */
  ossCategoryId?: number;
  /** 分类名称 */
  categoryName?: string;
  /** 父级分类id */
  parentId?: number;
  /** 显示顺序 */
  orderNum?: number;
  /** 用户类型 */
  userType?: string;
  /** 上传人 */
  createBy?: number;
}
/**
 * OSS分类视图对象
 */
export interface SysOssCategoryVo {
  /** oss分类id */
  ossCategoryId?: number;
  /** 分类名称 */
  categoryName?: string;
  /** 父级分类id */
  parentId?: number;
  /** 父级分类名称 */
  parentCategoryName?: string;
  /** 显示顺序 */
  orderNum?: number;
  /** 用户类型 */
  userType?: string;
  /** 上传人 */
  createBy?: number;
  /** 更新时间 */
  updateTime?: any;
  /** 创建时间 */
  createTime?: any;
  /** 子OSS分类 */
  children?: SysOssCategoryVo[];
}
