import type { R, TableDataInfo } from '@/api/model/resultModel';
import type {
  ProcessDefinitionQuery,
  ProcessDefinitionVo,
  DefinitionXmlVO,
} from '@/api/workflow/processDefinition/types';
import { request } from '@/utils/request';

/**
 * 获取流程定义列表
 * @param query 流程实例id
 * @returns
 */
export function listProcessDefinition(query: ProcessDefinitionQuery) {
  return request.get<TableDataInfo<ProcessDefinitionVo>>({
    url: `/workflow/processDefinition/list`,
    params: query,
  });
}
/**
 * 按照流程定义key获取流程定义
 * @param key 流程定义key
 * @returns
 */
export function getListByKey(key: string) {
  return request.get<R<ProcessDefinitionVo[]>>({
    url: `/workflow/processDefinition/getListByKey/${key}`,
  });
}

/**
 * 通过流程定义id获取流程图
 */
export function definitionImage(processDefinitionId: string) {
  return request.get<R<string>>({
    url: `/workflow/processDefinition/definitionImage/${processDefinitionId}?t${Math.random()}`,
  });
}

/**
 * 通过流程定义id获取xml
 * @param processDefinitionId 流程定义id
 * @returns
 */
export function definitionXml(processDefinitionId: string) {
  return request.get<R<DefinitionXmlVO>>({
    url: `/workflow/processDefinition/definitionXml/${processDefinitionId}`,
  });
}

/**
 * 删除流程定义
 * @param processDefinitionId 流程定义id
 * @param deploymentId 部署id
 */
export function deleteProcessDefinition(deploymentId: string, processDefinitionId: string) {
  return request.delete<R>({
    url: `/workflow/processDefinition/${deploymentId}/${processDefinitionId}`,
  });
}

/**
 * 挂起/激活
 * @param processDefinitionId 流程定义id
 * @returns
 */
export function updateDefinitionState(processDefinitionId: string) {
  return request.put<R>({
    url: `/workflow/processDefinition/updateDefinitionState/${processDefinitionId}`,
  });
}

/**
 * 流程定义转换为模型
 * @param processDefinitionId 流程定义id
 * @returns
 */
export function convertToModel(processDefinitionId: string) {
  return request.put<R>({
    url: `/workflow/processDefinition/convertToModel/${processDefinitionId}`,
  });
}

/**
 * 通过zip或xml部署流程定义
 * @returns
 */
export function deployProcessFile(data: any) {
  return request.post<R>({
    url: '/workflow/processDefinition/deployByFile',
    data,
  });
}

/**
 * 迁移流程
 * @param currentProcessDefinitionId
 * @param fromProcessDefinitionId
 * @returns
 */
export function migrationDefinition(currentProcessDefinitionId: string, fromProcessDefinitionId: string) {
  return request.put<R>({
    url: `/workflow/processDefinition/migrationDefinition/${currentProcessDefinitionId}/${fromProcessDefinitionId}`,
  });
}
