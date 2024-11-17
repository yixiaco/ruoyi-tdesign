import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 表单管理查询对象
 */
export interface WfFormManageQuery extends BaseEntity {
  /** 表单名称 */
  formName?: string;
  /** 表单类型 */
  formType?: string;
}
/**
 * 表单管理业务对象
 */
export interface WfFormManageForm {
  /** 主键 */
  id?: number;
  /** 表单名称 */
  formName?: string;
  /** 表单类型 */
  formType?: string;
  /** 路由地址/表单ID */
  router?: string;
  /** 备注 */
  remark?: string;
}
/**
 * 表单管理视图对象
 */
export interface WfFormManageVo {
  /** 主键 */
  id?: number;
  /** 表单名称 */
  formName?: string;
  /** 表单类型 */
  formType?: string;
  /** 表单类型名称 */
  formTypeName?: string;
  /** 路由地址/表单ID */
  router?: string;
  /** 备注 */
  remark?: string;
  /** 创建时间 */
  createTime?: any;
  /** 更新时间 */
  updateTime?: any;
}
