import type { BaseEntity } from '@/api/model/resultModel';

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
  /** 模板ID */
  templateId?: string;
  /** 平台标识 */
  supplierType?: string;
  /** 是否成功 */
  isSuccess?: number;
}
/**
 * 消息发送记录对象 sys_message_log
 */
export interface SysMessageLog {
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
  /** 模板ID */
  templateId?: string;
  /** 发送内容 */
  content?: string;
  /** 消息配置标题 */
  messageConfigTitle?: string;
  /** 平台标识 */
  supplierType?: string;
  /** 是否成功 */
  isSuccess?: number;
  /** 返回主体消息 */
  responseBody?: string;
  /** 记录时间 */
  logTime?: any;
  /** 消耗时间 */
  costTime?: number;
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
  /** 模板ID */
  templateId?: string;
  /** 发送内容 */
  content?: string;
  /** 消息配置标题 */
  messageConfigTitle?: string;
  /** 平台标识 */
  supplierType?: string;
  /** 是否成功 */
  isSuccess?: number;
  /** 返回主体消息 */
  responseBody?: string;
  /** 记录时间 */
  logTime?: any;
  /** 消耗时间 */
  costTime?: number;
}
