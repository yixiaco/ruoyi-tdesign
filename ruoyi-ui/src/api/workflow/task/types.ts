import type { BaseEntity, BasePageQuery } from '@/api/model/resultModel';

export interface TaskQuery extends BasePageQuery {
  name?: string;
  processDefinitionKey?: string;
  processDefinitionName?: string;
}

export interface ParticipantVo {
  groupIds?: string[] | number[];
  candidate: string[] | number[];
  candidateName: string[];
  claim: boolean;
}

export interface TaskVo extends BaseEntity {
  id: string;
  name: string;
  description?: string;
  priority: number;
  owner?: string;
  assignee?: string | number;
  assigneeName?: string;
  processInstanceId: string;
  executionId: string;
  taskDefinitionId?: any;
  processDefinitionId: string;
  endTime?: string;
  taskDefinitionKey: string;
  dueDate?: string;
  category?: any;
  parentTaskId?: any;
  tenantId: string;
  claimTime?: string;
  businessStatus: string;
  businessStatusName: string;
  processDefinitionName: string;
  processDefinitionKey: string;
  participantVo: ParticipantVo;
  multiInstance: boolean;
}

/**
 * 启动流程对象
 */
export interface StartProcessBo {
  /** 业务唯一值id */
  businessKey?: string;
  /** 流程执行key */
  processKey?: string;
  /** 流程变量，前端会提交一个元素{'entity': {业务详情数据对象}} */
  variables: Record<string, any>;
}

/**
 * 办理任务请求对象
 */
export interface CompleteTaskBo {
  /** 任务id */
  taskId?: string;
  /** 附件id */
  fileId?: string;
  /** 抄送人员 */
  wfCopyList?: WfCopy[];
  /** 消息类型 */
  messageType?: string[];
  /** 办理意见 */
  message?: string;
  /** 流程变量 */
  variables?: Record<string, any>;
}

/**
 * 抄送
 */
export interface WfCopy {
  /** 用户id */
  userId?: number;
  /** 用户名称 */
  userName?: string;
}

/**
 * 驳回参数请求
 */
export interface BackProcessBo {
  /** 任务ID */
  taskId?: string;
  /** 消息类型 */
  messageType?: string[];
  /** 驳回的节点id(目前未使用，直接驳回到申请人) */
  targetActivityId?: string;
  /** 办理意见 */
  message?: string;
}

/**
 * 加签参数请求
 */
export interface AddMultiBo {
  /** 任务ID */
  taskId?: string;
  /** 加签人员id */
  assignees?: number[];
  /** 加签人员名称 */
  assigneeNames?: string[];
}

/**
 * 减签参数请求
 */
export interface DeleteMultiBo {
  /** 任务ID */
  taskId?: string;
  /** 减签人员 */
  taskIds?: string[];
  /** 执行id */
  executionIds?: string[];
  /** 人员id */
  assigneeIds?: number[];
  /** 人员名称 */
  assigneeNames?: string[];
}

export interface StartWorkFlowResult {
  processInstanceId?: string;
  taskId?: string;
}
