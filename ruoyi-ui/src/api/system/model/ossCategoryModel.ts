import type { BaseEntity } from '@/api/model/resultModel';

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
  /** 文件最大字节长度 */
  maxSize?: number;
  /** 内容类型 */
  contentTypes?: string[];
  /** 多个文件后缀，忽略大小写 */
  suffixes?: string[];
  /** oss分类id */
  ossCategoryId?: number;
}
/**
 * OSS分类业务对象
 */
export interface SysOssCategoryForm {
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
  /** 分类路径 */
  categoryPath?: string;
  /** 层级 */
  level?: number;
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
  /** 文件数量 */
  fileCount?: number;
  /** 子OSS分类 */
  children?: SysOssCategoryVo[];
}
