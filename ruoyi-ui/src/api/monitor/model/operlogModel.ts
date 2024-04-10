import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 操作日志记录查询对象
 */
export interface SysOperLogQuery extends BaseEntity {
  /** 用户id */
  userId?: number;
  /** 模块标题 */
  title?: string;
  /** 业务类型 */
  businessType?: number;
  /** 操作人员 */
  operName?: string;
  /** 主机地址 */
  operIp?: string;
  /** 操作状态 */
  status?: number;
}
/**
 * 操作日志记录视图对象
 */
export interface SysOperLogVo {
  /** 日志主键 */
  operId?: number;
  /** 租户编号 */
  tenantId?: string;
  /** 用户id */
  userId?: number;
  /** 模块标题 */
  title?: string;
  /** 业务类型（0其它 1新增 2修改 3删除） */
  businessType?: number;
  /** 业务类型数组 */
  businessTypes?: number[];
  /** 方法名称 */
  method?: string;
  /** 请求方式 */
  requestMethod?: string;
  /** 操作类别（0其它 1后台用户 2手机端用户） */
  operatorType?: number;
  /** 操作人员 */
  operName?: string;
  /** 部门名称 */
  deptName?: string;
  /** 请求URL */
  operUrl?: string;
  /** 主机地址 */
  operIp?: string;
  /** 操作地点 */
  operLocation?: string;
  /** 请求参数 */
  operParam?: string;
  /** 返回参数 */
  jsonResult?: string;
  /** 操作状态（1正常 0异常） */
  status?: number;
  /** 错误消息 */
  errorMsg?: string;
  /** 操作时间 */
  operTime?: any;
  /** 消耗时间(ms) */
  costTime?: number;
}
