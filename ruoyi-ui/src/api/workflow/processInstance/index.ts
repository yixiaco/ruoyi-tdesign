import type { R, TableDataInfo } from '@/api/model/resultModel';
import type { ProcessInstanceQuery, ProcessInstanceVo } from '@/api/workflow/model/processInstanceModel';
import { request } from '@/utils/request';

/**
 * 查询运行中实例列表
 * @param query
 */
export function getPageByRunning(query: ProcessInstanceQuery) {
  return request.get<TableDataInfo<ProcessInstanceVo>>({
    url: '/workflow/processInstance/getPageByRunning',
    params: query,
  });
}

/**
 * 查询已完成实例列表
 * @param query
 */
export function getPageByFinish(query: ProcessInstanceQuery) {
  return request.get<TableDataInfo<ProcessInstanceVo>>({
    url: '/workflow/processInstance/getPageByFinish',
    params: query,
  });
}

/**
 * 通过流程实例id获取历史流程图
 */
export function getHistoryImage(processInstanceId: string) {
  return request.get<R<string>>({
    url: `/workflow/processInstance/getHistoryImage/${processInstanceId}?t${Math.random()}`,
  });
}

/**
 * 通过流程实例id获取历史流程图运行中，历史等节点
 */
export function getHistoryList(instanceId: string) {
  return request.get<R<Record<string, any>>>({
    url: `/workflow/processInstance/getHistoryList/${instanceId}?t${Math.random()}`,
  });
}

/**
 * 获取审批记录
 * @param processInstanceId 流程实例id
 * @returns
 */
export function getHistoryRecord(processInstanceId: string) {
  return request.get({
    url: `/workflow/processInstance/getHistoryRecord/${processInstanceId}`,
  });
}

/**
 * 作废
 * @param data 参数
 * @returns
 */
export function deleteRunInstance(data: object) {
  return request.post<R>({
    url: `/workflow/processInstance/deleteRunInstance`,
    data,
  });
}

/**
 * 运行中的实例 删除程实例，删除历史记录，删除业务与流程关联信息
 * @param processInstanceId 流程实例id
 * @returns
 */
export function deleteRunAndHisInstance(processInstanceId: string | string[]) {
  return request.delete<R>({
    url: `/workflow/processInstance/deleteRunAndHisInstance/${processInstanceId}`,
  });
}

/**
 * 已完成的实例 删除程实例，删除历史记录，删除业务与流程关联信息
 * @param processInstanceId 流程实例id
 * @returns
 */
export function deleteFinishAndHisInstance(processInstanceId: string | string[]) {
  return request.delete<R>({
    url: `/workflow/processInstance/deleteFinishAndHisInstance/${processInstanceId}`,
  });
}

/**
 * 分页查询当前登录人单据
 * @param query
 */
export function getPageByCurrent(query: ProcessInstanceQuery) {
  return request.get<TableDataInfo<ProcessInstanceVo>>({
    url: '/workflow/processInstance/getPageByCurrent',
    params: query,
  });
}

/**
 * 撤销流程
 * @param processInstanceId 流程实例id
 * @returns
 */
export function cancelProcessApply(processInstanceId: string) {
  return request.post<R>({
    url: `/workflow/processInstance/cancelProcessApply/${processInstanceId}`,
  });
}

export default {
  getPageByRunning,
  getPageByFinish,
  getHistoryImage,
  getHistoryList,
  getHistoryRecord,
  deleteRunInstance,
  deleteRunAndHisInstance,
  deleteFinishAndHisInstance,
  getPageByCurrent,
  cancelProcessApply,
};
