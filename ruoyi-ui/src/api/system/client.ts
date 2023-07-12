import { R, TableDataInfo } from '@/api/model/resultModel';
import { ClientForm, ClientQuery, ClientVO } from '@/api/system/model/clientModel';
import { request } from '@/utils/request';

/**
 * 查询客户端管理列表
 * @param query
 * @returns {*}
 */
export function listClient(query?: ClientQuery) {
  return request.get<TableDataInfo<ClientVO>>({
    url: '/system/client/list',
    params: query,
  });
}

/**
 * 查询客户端管理详细
 * @param id
 */
export function getClient(id: string | number) {
  return request.get<R<ClientVO>>({
    url: `/system/client/${id}`,
  });
}

/**
 * 新增客户端管理
 * @param data
 */
export function addClient(data: ClientForm) {
  return request.post({
    url: '/system/client',
    data,
  });
}

/**
 * 修改客户端管理
 * @param data
 */
export function updateClient(data: ClientForm) {
  return request.put({
    url: '/system/client',
    data,
  });
}

/**
 * 删除客户端管理
 * @param id
 */
export function delClient(id: string | number | Array<string | number>) {
  return request.delete({
    url: `/system/client/${id}`,
  });
}

/**
 * 状态修改
 * @param id ID
 * @param status 状态
 */
export function changeStatus(id: number | string, status: string) {
  const data = {
    id,
    status,
  };
  return request.put({
    url: '/system/client/changeStatus',
    data,
  });
}
