import { R, TableDataInfo } from '@/api/model/resultModel';
import { SysOperLogBo, SysOperLogVo } from '@/api/monitor/model/operlogModel';
import { request } from '@/utils/request';

// 查询操作日志列表
export function list(query: SysOperLogBo) {
  return request.get<TableDataInfo<SysOperLogVo>>({
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
