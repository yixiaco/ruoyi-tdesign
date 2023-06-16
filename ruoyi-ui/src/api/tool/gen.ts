import { R, TableDataInfo } from '@/api/model/resultModel';
import { GenTable, GenTableInfo } from '@/api/tool/model/genModel';
import { request } from '@/utils/request';

// 查询生成表数据
export function listTable(query: GenTable) {
  return request.get<TableDataInfo<GenTable>>({
    url: '/tool/gen/list',
    params: query,
  });
}

// 查询db数据库列表
export function listDbTable(query: GenTable) {
  return request.get<TableDataInfo<GenTable>>({
    url: '/tool/gen/db/list',
    params: query,
  });
}

// 查询表详细信息
export function getGenTable(tableId: number | string) {
  return request.get<R<GenTableInfo>>({
    url: `/tool/gen/${tableId}`,
  });
}

// 修改代码生成信息
export function updateGenTable(data: GenTable) {
  return request.put<R<void>>({
    url: '/tool/gen',
    data,
  });
}

// 导入表
export function importTable(tables: string, dataName: string) {
  return request.post<R<void>>({
    url: '/tool/gen/importTable',
    params: { tables, dataName },
  });
}

// 预览生成代码
export function previewTable(tableId: number) {
  return request.get<R<Record<string, string>>>({
    url: `/tool/gen/preview/${tableId}`,
  });
}

// 删除表数据
export function delTable(tableId: number | number[]) {
  return request.delete<R<void>>({
    url: `/tool/gen/${tableId}`,
  });
}

// 生成代码（自定义路径）
export function genCode(tableId: string | number) {
  return request.get({
    url: `/tool/gen/genCode/${tableId}`,
  });
}

// 同步数据库
export function synchDb(tableId: string | number) {
  return request.get<R<void>>({
    url: `/tool/gen/synchDb/${tableId}`,
  });
}

// 获取数据源名称
export function getDataNames() {
  return request.get<R<string[]>>({
    url: '/tool/gen/getDataNames',
  });
}
