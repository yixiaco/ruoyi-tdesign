import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 流程分类查询对象
 */
export interface WfCategoryQuery extends BaseEntity {
  /** 分类名称 */
  categoryName?: string;
  /** 分类编码 */
  categoryCode?: string;
}
/**
 * 流程分类业务对象
 */
export interface WfCategoryForm {
  /** 主键 */
  id?: number;
  /** 分类名称 */
  categoryName?: string;
  /** 分类编码 */
  categoryCode?: string;
  /** 父级id */
  parentId?: number;
  /** 排序 */
  sortNum?: number;
}
/**
 * 流程分类视图对象
 */
export interface WfCategoryVo {
  /** 主键 */
  id?: number;
  /** 分类名称 */
  categoryName?: string;
  /** 分类编码 */
  categoryCode?: string;
  /** 父级id */
  parentId?: number;
  /** 排序 */
  sortNum?: number;
  /** 创建时间 */
  createTime?: any;
  /** 更新时间 */
  updateTime?: any;
  /** 子流程分类 */
  children?: WfCategoryVo[];
}
