import type { R, TableDataInfo } from '@/api/model/resultModel';
import type { ProcessInstanceQuery, ProcessInstanceVo } from '@/api/workflow/model/processInstanceModel';
import { request } from '@/utils/request';

/**
 * 查询运行中实例列表
 * @param query
 */
export function getProcessInstanceRunningByPage(query: ProcessInstanceQuery) {
  return request.get<TableDataInfo<ProcessInstanceVo>>({
    url: '/workflow/processInstance/getProcessInstanceRunningByPage',
    params: query,
  });
}

/**
 * 查询已完成实例列表
 * @param query
 */
export function getProcessInstanceFinishByPage(query: ProcessInstanceQuery) {
  return request.get<TableDataInfo<ProcessInstanceVo>>({
    url: '/workflow/processInstance/getProcessInstanceFinishByPage',
    params: query,
  });
}

/**
 * 通过流程实例id获取历史流程图
 */
export function getHistoryProcessImage(processInstanceId: string) {
  return request.get<R<string>>({
    url: `/workflow/processInstance/getHistoryProcessImage/${processInstanceId}?t${Math.random()}`,
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
export function deleteRuntimeProcessInst(data: object) {
  return request.post<R>({
    url: `/workflow/processInstance/deleteRuntimeProcessInst`,
    data,
  });
}

/**
 * 运行中的实例 删除程实例，删除历史记录，删除业务与流程关联信息
 * @param processInstanceId 流程实例id
 * @returns
 */
export function deleteRuntimeProcessAndHisInst(processInstanceId: string | string[]) {
  return request.delete<R>({
    url: `/workflow/processInstance/deleteRuntimeProcessAndHisInst/${processInstanceId}`,
  });
}

/**
 * 已完成的实例 删除程实例，删除历史记录，删除业务与流程关联信息
 * @param processInstanceId 流程实例id
 * @returns
 */
export function deleteFinishProcessAndHisInst(processInstanceId: string | string[]) {
  return request.delete<R>({
    url: `/workflow/processInstance/deleteFinishProcessAndHisInst/${processInstanceId}`,
  });
}

/**
 * 分页查询当前登录人单据
 * @param query
 * @returns {*}
 */
export function getCurrentSubmitByPage(query: ProcessInstanceQuery) {
  return request.get<TableDataInfo<ProcessInstanceVo>>({
    url: '/workflow/processInstance/getCurrentSubmitByPage',
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
