import type { R, TableDataInfo } from '@/api/model/resultModel';
import type { WfFormManageForm, WfFormManageQuery, WfFormManageVo } from '@/api/workflow/model/formManageModel';
import { request } from '@/utils/request';

/**
 * 查询表单管理列表
 * @param query
 */

export const listFormManage = (query?: WfFormManageQuery) => {
  return request.get<TableDataInfo<WfFormManageVo>>({
    url: '/workflow/formManage/list',
    params: query,
  });
};

/**
 * 查询表单管理列表
 * @param query
 */

export const selectListFormManage = () => {
  return request.get<R<WfFormManageVo[]>>({
    url: '/workflow/formManage/list/selectList',
  });
};

/**
 * 查询表单管理详细
 * @param id
 */
export const getFormManage = (id: string | number) => {
  return request.get<R<WfFormManageVo>>({
    url: `/workflow/formManage/${id}`,
  });
};

/**
 * 新增表单管理
 * @param data
 */
export const addFormManage = (data: WfFormManageForm) => {
  return request.post<R>({
    url: '/workflow/formManage',
    data,
  });
};

/**
 * 修改表单管理
 * @param data
 */
export const updateFormManage = (data: WfFormManageForm) => {
  return request.put<R>({
    url: '/workflow/formManage',
    data,
  });
};

/**
 * 删除表单管理
 * @param id
 */
export const delFormManage = (id: string | number | Array<string | number>) => {
  return request.delete<R>({
    url: `/workflow/formManage/${id}`,
  });
};
