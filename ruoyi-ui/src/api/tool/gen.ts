import { request } from '@/utils/request';
import { R, TableDataInfo } from '@/api/model/resultModel';
import { GenTable, GenTableInfo } from '@/api/tool/model/genModel';

// 查询生成表数据
export function listTable(query: GenTable) {
  return request.get<TableDataInfo<GenTable>>({
    headers: { datasource: localStorage.getItem('dataName') },
    url: '/tool/gen/list',
    params: query,
  });
}

// 查询db数据库列表
export function listDbTable(query: GenTable) {
  return request.get<TableDataInfo<GenTable>>({
    headers: { datasource: localStorage.getItem('dataName') },
    url: '/tool/gen/db/list',
    params: query,
  });
}

// 查询表详细信息
export function getGenTable(tableId: number | string) {
  return request.get<R<GenTableInfo>>({
    headers: { datasource: localStorage.getItem('dataName') },
    url: `/tool/gen/${tableId}`,
  });
}

// 修改代码生成信息
export function updateGenTable(data: GenTable) {
  return request.put<R<void>>({
    headers: { datasource: localStorage.getItem('dataName') },
    url: '/tool/gen',
    data,
  });
}

// 导入表
export function importTable(tables: string) {
  return request.post<R<void>>({
    headers: { datasource: localStorage.getItem('dataName') },
    url: '/tool/gen/importTable',
    params: { tables },
  });
}

// 预览生成代码
export function previewTable(tableId: number) {
  return request.get<R<Record<string, string>>>({
    headers: { datasource: localStorage.getItem('dataName') },
    url: `/tool/gen/preview/${tableId}`,
  });
}

// 删除表数据
export function delTable(tableId: number) {
  return request.delete<R<void>>({
    headers: { datasource: localStorage.getItem('dataName') },
    url: `/tool/gen/${tableId}`,
  });
}

// 生成代码（自定义路径）
export function genCode(tableName: string) {
  return request.get({
    headers: { datasource: localStorage.getItem('dataName') },
    url: `/tool/gen/genCode/${tableName}`,
  });
}

// 同步数据库
export function synchDb(tableName: string) {
  return request.get<R<void>>({
    headers: { datasource: localStorage.getItem('dataName') },
    url: `/tool/gen/synchDb/${tableName}`,
  });
}
