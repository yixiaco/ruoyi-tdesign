import { R, TableDataInfo } from '@/api/model/resultModel';
import { SysAppForm, SysAppQuery, SysAppVo } from '@/api/system/model/appModel';
import { request } from '@/utils/request';

// 查询应用管理列表
export function listApp(query?: SysAppQuery) {
  return request.get<TableDataInfo<SysAppVo>>({
    url: '/system/app/list',
    params: query,
  });
}

// 查询应用管理详细
export function getApp(appid: number) {
  return request.get<R<SysAppVo>>({
    url: `/system/app/${appid}`,
  });
}

// 新增应用管理
export function addApp(data: SysAppForm) {
  return request.post<R<void>>({
    url: '/system/app',
    data,
  });
}

// 修改应用管理
export function updateApp(data: SysAppForm) {
  return request.put<R<void>>({
    url: '/system/app',
    data,
  });
}

// 删除应用管理
export function delApp(appids: number | Array<number>) {
  return request.delete<R<void>>({
    url: `/system/app/${appids}`,
  });
}
