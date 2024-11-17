import type { R } from '@/api/model/resultModel';
import type { WfCategoryForm, WfCategoryQuery, WfCategoryVo } from '@/api/workflow/model/categoryModel';
import { request } from '@/utils/request';

// 查询流程分类列表
export function listCategory(query?: WfCategoryQuery) {
  return request.get<R<Array<WfCategoryVo>>>({
    url: '/workflow/category/list',
    params: query,
  });
}

// 查询流程分类详细
export function getCategory(id: number) {
  return request.get<R<WfCategoryVo>>({
    url: `/workflow/category/${id}`,
  });
}

// 新增流程分类
export function addCategory(data: WfCategoryForm) {
  return request.post<R>({
    url: '/workflow/category',
    data,
  });
}

// 修改流程分类
export function updateCategory(data: WfCategoryForm) {
  return request.put<R>({
    url: '/workflow/category',
    data,
  });
}

// 删除流程分类
export function delCategory(ids: number | Array<number>) {
  return request.delete<R>({
    url: `/workflow/category/${ids}`,
  });
}
