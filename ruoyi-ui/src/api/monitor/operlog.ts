import { request } from '@/utils/request';
import { SysOperLog } from '@/api/monitor/model/operlogModel';
import { R, TableDataInfo } from '@/api/model/resultModel';

// 查询操作日志列表
export function list(query: SysOperLog) {
  return request.get<TableDataInfo<SysOperLog>>({
    url: '/monitor/operlog/list',
    params: query,
  });
}

// 删除操作日志
export function delOperlog(operId: number) {
  return request.delete<R<void>>({
    url: `/monitor/operlog/${operId}`,
  });
}

// 清空操作日志
export function cleanOperlog() {
  return request.delete<R<void>>({
    url: '/monitor/operlog/clean',
  });
}
