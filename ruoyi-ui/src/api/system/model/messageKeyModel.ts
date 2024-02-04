import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 消息常量查询对象
 */
export interface SysMessageKeyQuery extends BaseEntity {
  /** 消息名称 */
  name?: string;
  /** 消息key */
  messageKey?: string;
}
/**
 * 消息常量业务对象
 */
export interface SysMessageKeyForm {
  /** 消息key主键 */
  messageKeyId?: number;
  /** 消息名称 */
  name?: string;
  /** 消息key */
  messageKey?: string;
  /** 备注 */
  remark?: string;
}
/**
 * 消息常量视图对象
 */
export interface SysMessageKeyVo {
  /** 消息key主键 */
  messageKeyId?: number;
  /** 消息名称 */
  name?: string;
  /** 消息key */
  messageKey?: string;
  /** 备注 */
  remark?: string;
  /** 更新时间 */
  updateTime?: any;
  /** 创建时间 */
  createTime?: any;
}
