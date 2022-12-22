import { request } from '@/utils/request';
import { R, TableDataInfo } from '@/api/model/resultModel';
import { SysDictData } from '@/api/system/model/dictModel';

// 查询字典数据列表
export function listData(query) {
  return request.get<TableDataInfo<SysDictData>>({
    url: '/system/dict/data/list',
    params: query,
  });
}

// 查询字典数据详细
export function getData(dictCode) {
  return request.get<R<SysDictData>>({
    url: `/system/dict/data/${dictCode}`,
  });
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
  return request.get<R<Array<SysDictData>>>({
    url: `/system/dict/data/type/${dictType}`,
  });
}

// 新增字典数据
export function addData(data) {
  return request.post<R<void>>({
    url: '/system/dict/data',
    data,
  });
}

// 修改字典数据
export function updateData(data) {
  return request.put<R<void>>({
    url: '/system/dict/data',
    data,
  });
}

// 删除字典数据
export function delData(dictCode) {
  return request.delete<R<void>>({
    url: `/system/dict/data/${dictCode}`,
  });
}
