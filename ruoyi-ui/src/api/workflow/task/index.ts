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
} from '@/api/workflow/task/types';
import { request } from '@/utils/request';

/**
 * 查询待办列表
 * @param query
 * @returns {*}
 */
export const getTaskWaitByPage = (query: TaskQuery) => {
  return request.get<TableDataInfo<TaskVo>>({
    url: '/workflow/task/getTaskWaitByPage',
    params: query,
  });
};

/**
 * 查询已办列表
 * @param query
 */
export const getTaskFinishByPage = (query: TaskQuery) => {
  return request.get<TableDataInfo<TaskVo>>({
    url: '/workflow/task/getTaskFinishByPage',
    params: query,
  });
};

/**
 * 查询当前用户的抄送列表
 * @param query
 */
export const getTaskCopyByPage = (query: TaskQuery) => {
  return request.get<TableDataInfo<TaskVo>>({
    url: '/workflow/task/getTaskCopyByPage',
    params: query,
  });
};

/**
 * 当前租户所有待办任务
 * @param query
 */
export const getAllTaskWaitByPage = (query: TaskQuery) => {
  return request.get<TableDataInfo<TaskVo>>({
    url: '/workflow/task/getAllTaskWaitByPage',
    params: query,
  });
};

/**
 * 当前租户所有已办任务
 * @param query
 */
export const getAllTaskFinishByPage = (query: TaskQuery) => {
  return request.get<TableDataInfo<TaskVo>>({
    url: '/workflow/task/getAllTaskFinishByPage',
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
 * 获取流程状态
 * @param taskId
 * @returns
 */
export const getBusinessStatus = (taskId: string) => {
  return request.get<R<string>>({
    url: `/workflow/task/getBusinessStatus/${taskId}`,
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
