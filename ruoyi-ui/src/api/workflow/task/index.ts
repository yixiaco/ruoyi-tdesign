import type { R, TableDataInfo } from '@/api/model/resultModel';
import type {
  AddMultiBo,
  BackProcessBo,
  CompleteTaskBo,
  DeleteMultiBo,
  StartProcessBo,
  StartWorkFlowResult,
  TaskQuery,
  TaskVo,
  VariableVo,
  WfTaskBackNode,
} from '@/api/workflow/task/types';
import { request } from '@/utils/request';

/**
 * 查询待办列表
 * @param query
 */
export const getPageByTaskWait = (query: TaskQuery) => {
  return request.get<TableDataInfo<TaskVo>>({
    url: '/workflow/task/getPageByTaskWait',
    params: query,
  });
};

/**
 * 查询已办列表
 * @param query
 */
export const getPageByTaskFinish = (query: TaskQuery) => {
  return request.get<TableDataInfo<TaskVo>>({
    url: '/workflow/task/getPageByTaskFinish',
    params: query,
  });
};

/**
 * 查询当前用户的抄送列表
 * @param query
 */
export const getPageByTaskCopy = (query: TaskQuery) => {
  return request.get<TableDataInfo<TaskVo>>({
    url: '/workflow/task/getPageByTaskCopy',
    params: query,
  });
};

/**
 * 当前租户所有待办任务
 * @param query
 */
export const getPageByAllTaskWait = (query: TaskQuery) => {
  return request.get<TableDataInfo<TaskVo>>({
    url: '/workflow/task/getPageByAllTaskWait',
    params: query,
  });
};

/**
 * 当前租户所有已办任务
 * @param query
 */
export const getPageByAllTaskFinish = (query: TaskQuery) => {
  return request.get<TableDataInfo<TaskVo>>({
    url: '/workflow/task/getPageByAllTaskFinish',
    params: query,
  });
};

/**
 * 启动流程
 * @param data
 */
export const startWorkFlow = (data: StartProcessBo) => {
  return request.post<R<StartWorkFlowResult>>({
    url: '/workflow/task/startWorkFlow',
    data,
  });
};

/**
 * 办理流程
 * @param data
 */
export const completeTask = (data: CompleteTaskBo) => {
  return request.post<R>({
    url: '/workflow/task/completeTask',
    data,
  });
};

/**
 * 认领任务
 * @param taskId
 */
export const claim = (taskId: string) => {
  return request.post<R>({
    url: `/workflow/task/claim/${taskId}`,
  });
};

/**
 * 归还任务
 * @param taskId
 */
export const returnTask = (taskId: string) => {
  return request.post<R>({
    url: `/workflow/task/returnTask/${taskId}`,
  });
};

/**
 * 任务驳回
 * @param data
 */
export const backProcess = (data: BackProcessBo) => {
  return request.post<R<string>>({
    url: '/workflow/task/backProcess',
    data,
  });
};

/**
 * 获取当前任务
 * @param taskId
 * @returns
 */
export const getTaskById = (taskId: string) => {
  return request.get<R<TaskVo>>({
    url: `/workflow/task/getTaskById/${taskId}`,
  });
};

/**
 * 加签
 * @param data
 * @returns
 */
export const addMultiInstanceExecution = (data: AddMultiBo) => {
  return request.post<R>({
    url: '/workflow/task/addMultiInstanceExecution',
    data,
  });
};

/**
 * 减签
 * @param data
 * @returns
 */
export const deleteMultiInstanceExecution = (data: DeleteMultiBo) => {
  return request.post<R>({
    url: '/workflow/task/deleteMultiInstanceExecution',
    data,
  });
};

/**
 * 修改任务办理人
 * @param taskIds
 * @param userId
 * @returns
 */
export const updateAssignee = (taskIds: Array<string>, userId: string | number) => {
  return request.put<R>({
    url: `/workflow/task/updateAssignee/${taskIds}/${userId}`,
  });
};

/**
 * 转办任务
 * @returns
 * @param data
 */
export function transferTask(data: object) {
  return request.post<R>({
    url: `/workflow/task/transferTask`,
    data,
  });
}

/**
 * 终止任务
 * @returns
 */
export function terminationTask(data: any) {
  return request.post({
    url: `/workflow/task/terminationTask`,
    data,
  });
}

/**
 * 查询流程变量
 * @returns
 */
export function getInstanceVariable(taskId: string) {
  return request.get<R<VariableVo[]>>({
    url: `/workflow/task/getInstanceVariable/${taskId}`,
  });
}

/**
 * 获取可驳回得任务节点
 */
export function getTaskNodeList(processInstanceId: string) {
  return request.get<R<WfTaskBackNode[]>>({
    url: `/workflow/task/getTaskNodeList/${processInstanceId}`,
  });
}

/**
 * 委托任务
 */
export function delegateTask(data: any) {
  return request.post<R>({
    url: `/workflow/task/delegateTask`,
    data,
  });
}

/**
 * 查询工作流任务用户选择加签人员
 * @param taskId
 */
export function getTaskUserIdsByAddMultiInstance(taskId: string) {
  return request.get<R<string>>({
    url: `/workflow/task/getTaskUserIdsByAddMultiInstance/${taskId}`,
  });
}

/**
 * 查询工作流选择减签人员
 * @param taskId
 */
export function getListByDeleteMultiInstance(taskId: string) {
  return request.get<R<TaskVo[]>>({
    url: `/workflow/task/getListByDeleteMultiInstance/${taskId}`,
  });
}
