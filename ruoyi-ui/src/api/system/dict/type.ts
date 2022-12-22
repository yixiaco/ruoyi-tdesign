import { request } from '@/utils/request';
import { R, TableDataInfo } from '@/api/model/resultModel';
import { SysDictType } from '@/api/system/model/dictModel';

// 查询字典类型列表
export function listType(query) {
  return request.get<TableDataInfo<SysDictType>>({
    url: '/system/dict/type/list',
    params: query,
  });
}

// 查询字典类型详细
export function getType(dictId) {
  return request.get<R<SysDictType>>({
    url: `/system/dict/type/${dictId}`,
  });
}

// 新增字典类型
export function addType(data) {
  return request.post<R<void>>({
    url: '/system/dict/type',
    data,
  });
}

// 修改字典类型
export function updateType(data) {
  return request.put<R<void>>({
    url: '/system/dict/type',
    data,
  });
}

// 删除字典类型
export function delType(dictId) {
  return request.delete<R<void>>({
    url: `/system/dict/type/${dictId}`,
  });
}

// 刷新字典缓存
export function refreshCache() {
  return request.delete<R<void>>({
    url: '/system/dict/type/refreshCache',
  });
}

// 获取字典选择框列表
export function optionselect() {
  return request.get<R<Array<SysDictType>>>({
    url: '/system/dict/type/optionselect',
  });
}
