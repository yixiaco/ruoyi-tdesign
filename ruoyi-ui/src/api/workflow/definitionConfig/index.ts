import type { R } from '@/api/model/resultModel';
import type { DefinitionConfigForm, DefinitionConfigVO } from '@/api/workflow/definitionConfig/types';
import { request } from '@/utils/request';

/**
 * 查询表单配置详细
 * @param definitionId
 */
export const getByDefId = (definitionId: string | number) => {
  return request.get<R<DefinitionConfigVO>>({
    url: `/workflow/definitionConfig/getByDefId/${definitionId}`,
  });
};

/**
 * 新增表单配置
 * @param data
 */
export const saveOrUpdate = (data: DefinitionConfigForm) => {
  return request.post<R>({
    url: '/workflow/definitionConfig/saveOrUpdate',
    data,
  });
};

/**
 * 删除表单配置
 * @param id
 */
export const deldefinitionConfig = (id: string | number | Array<string | number>) => {
  return request.delete<R>({
    url: `/workflow/definitionConfig/${id}`,
  });
};

/**
 * 查询流程定义配置排除当前查询的流程定义
 * @param tableName
 * @param definitionId
 */
export const getByTableNameNotDefId = (tableName: string, definitionId: string | number) => {
  return request.get<R<DefinitionConfigVO[]>>({
    url: `/workflow/definitionConfig/getByTableNameNotDefId/${tableName}/${definitionId}`,
  });
};
