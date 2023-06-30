import { BaseEntity } from '@/api/model/resultModel';

/**
 * 消息发送记录查询对象
 */
export interface SysMessageLogQuery extends BaseEntity {
  /** 消息模板id */
  messageTemplateId?: number;
  /** 消息key */
  messageKey?: string;
  /** 模板名称 */
  messageTemplateName?: string;
  /** 消息类型 */
  messageType?: string;
  /** 模板类型 */
  templateMode?: string;
  /** 发送账号 */
  account?: string;
  /** 模板id */
  templateId?: string;
  /** 平台标识 */
  supplierType?: string;
  /** 是否成功 */
  isSuccess?: number;
}
/**
 * 消息发送记录对象 sys_message_log
 */
export interface SysMessageLog extends BaseEntity {
  /** 消息发送记录id */
  messageLogId?: number;
  /** 消息模板id */
  messageTemplateId?: number;
  /** 消息key */
  messageKey?: string;
  /** 模板名称 */
  messageTemplateName?: string;
  /** 消息类型 */
  messageType?: string;
  /** 模板类型 */
  templateMode?: string;
  /** 发送账号 */
  account?: string;
  /** 标题 */
  title?: string;
  /** 模板id */
  templateId?: string;
  /** 发送内容 */
  content?: string;
  /** 消息配置标题 */
  messageConfigTitle?: string;
  /** 平台标识 */
  supplierType?: string;
  /** 是否成功 */
  isSuccess?: number;
  /** 错误码 */
  errorCode?: string;
  /** 错误消息 */
  errorMessage?: string;
  /** 回执消息id */
  bizId?: string;
  /** 返回消息 */
  message?: string;
  /** 记录时间 */
  logTime?: any;
}
/**
 * 消息发送记录视图对象
 */
export interface SysMessageLogVo {
  /** 消息发送记录id */
  messageLogId?: number;
  /** 消息模板id */
  messageTemplateId?: number;
  /** 消息key */
  messageKey?: string;
  /** 模板名称 */
  messageTemplateName?: string;
  /** 消息类型 */
  messageType?: string;
  /** 模板类型 */
  templateMode?: string;
  /** 发送账号 */
  account?: string;
  /** 标题 */
  title?: string;
  /** 模板id */
  templateId?: string;
  /** 发送内容 */
  content?: string;
  /** 消息配置标题 */
  messageConfigTitle?: string;
  /** 平台标识 */
  supplierType?: string;
  /** 是否成功 */
  isSuccess?: number;
  /** 错误码 */
  errorCode?: string;
  /** 错误消息 */
  errorMessage?: string;
  /** 回执消息id */
  bizId?: string;
  /** 返回消息 */
  message?: string;
  /** 记录时间 */
  logTime?: any;
}
