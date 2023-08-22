import type { R, TableDataInfo } from '@/api/model/resultModel';
import type { SysClientForm, SysClientQuery, SysClientVo } from '@/api/system/model/clientModel';
import { request } from '@/utils/request';

// 查询系统授权列表
export function listClient(query?: SysClientQuery) {
  return request.get<TableDataInfo<SysClientVo>>({
    url: '/system/client/list',
    params: query,
  });
}

// 查询系统授权详细
export function getClient(id: number) {
  return request.get<R<SysClientVo>>({
    url: `/system/client/${id}`,
  });
}

// 新增系统授权
export function addClient(data: SysClientForm) {
  return request.post<R<void>>({
    url: '/system/client',
    data,
  });
}

// 修改系统授权
export function updateClient(data: SysClientForm) {
  return request.put<R<void>>({
    url: '/system/client',
    data,
  });
}

// 删除系统授权
export function delClient(ids: number | Array<number>) {
  return request.delete<R<void>>({
    url: `/system/client/${ids}`,
  });
}

/**
 * 状态修改
 * @param id ID
 * @param status 状态
 */
export function changeStatus(id: number, status: string) {
  const data = {
    id,
    status,
  };
  return request.put({
    url: '/system/client/changeStatus',
    data,
  });
}
