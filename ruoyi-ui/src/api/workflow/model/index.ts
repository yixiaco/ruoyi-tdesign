import type { R, TableDataInfo } from '@/api/model/resultModel';
import type { ModelForm, ModelQuery, ModelVO } from '@/api/workflow/model/types';
import { request } from '@/utils/request';

/**
 * 查询模型列表
 * @param query
 */
export function listModel(query: ModelQuery) {
  return request.get<TableDataInfo<ModelVO>>({
    url: '/workflow/model/list',
    params: query,
  });
}

/**
 * 查询模型信息
 * @param id
 */
export function getModelInfo(id: string) {
  return request.get<R<ModelVO>>({
    url: `/workflow/model/getInfo/${id}`,
  });
}

/**
 * 新增模型
 * @param data
 */
export function addModel(data: ModelForm) {
  return request.post<R>({
    url: '/workflow/model/save',
    data,
  });
}

/**
 * 修改模型信息
 * @param data
 */
export function updateModel(data: ModelForm) {
  return request.put<R>({
    url: '/workflow/model/update',
    data,
  });
}

/**
 * 修改模型信息
 * @param data
 */
export function editModelXml(data: ModelForm) {
  return request.put<R>({
    url: '/workflow/model/editModelXml',
    data,
  });
}

/**
 * 按id删除模型
 * @param id 模型id
 */
export function delModel(id: string | string[]) {
  return request.delete<R>({
    url: `/workflow/model/${id}`,
  });
}

/**
 * 模型部署
 * @param id 模型id
 */
export function modelDeploy(id: string) {
  return request.post<R>({
    url: `/workflow/model/modelDeploy/${id}`,
  });
}

/**
 * 复制模型
 * @param data
 */
export function copyModel(data: ModelForm) {
  return request.post<R>({
    url: '/workflow/model/copyModel',
    data,
  });
}
