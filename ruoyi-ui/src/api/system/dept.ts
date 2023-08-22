import type { R } from '@/api/model/resultModel';
import type { SysDeptForm, SysDeptQuery, SysDeptVo } from '@/api/system/model/deptModel';
import { request } from '@/utils/request';

// 查询部门列表
export function listDept(query?: SysDeptQuery) {
  return request.get<R<Array<SysDeptVo>>>({
    url: '/system/dept/list',
    params: query,
  });
}

// 查询部门列表（排除节点）
export function listDeptExcludeChild(deptId: number) {
  return request.get<R<Array<SysDeptVo>>>({
    url: `/system/dept/list/exclude/${deptId}`,
  });
}

// 查询部门详细
export function getDept(deptId: number) {
  return request.get<R<SysDeptVo>>({
    url: `/system/dept/${deptId}`,
  });
}

// 新增部门
export function addDept(data: SysDeptForm) {
  return request.post<R<void>>({
    url: '/system/dept',
    data,
  });
}

// 修改部门
export function updateDept(data: SysDeptForm) {
  return request.put<R<void>>({
    url: '/system/dept',
    data,
  });
}

// 删除部门
export function delDept(deptId: number) {
  return request.delete<R<void>>({
    url: `/system/dept/${deptId}`,
  });
}
