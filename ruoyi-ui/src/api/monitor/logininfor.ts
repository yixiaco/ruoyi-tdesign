import type { R, TableDataInfo } from '@/api/model/resultModel';
import type { SysLogininforBo, SysLogininforVo } from '@/api/monitor/model/logininforModel';
import { request } from '@/utils/request';

// 查询登录日志列表
export function list(query: SysLogininforBo) {
  return request.get<TableDataInfo<SysLogininforVo>>({
    url: '/monitor/logininfor/list',
    params: query,
  });
}

// 删除登录日志
export function delLogininfor(infoId: number | number[]) {
  return request.delete<R<void>>({
    url: `/monitor/logininfor/${infoId}`,
  });
}

// 解锁用户登录状态
export function unlockLogininfor(userName: string) {
  return request.get<R<void>>({
    url: `/monitor/logininfor/unlock/${userName}`,
  });
}

// 清空登录日志
export function cleanLogininfor() {
  return request.delete<R<void>>({
    url: '/monitor/logininfor/clean',
  });
}
