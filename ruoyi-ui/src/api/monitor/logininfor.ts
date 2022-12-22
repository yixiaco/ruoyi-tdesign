import { request } from '@/utils/request';
import { SysLogininfor } from '@/api/monitor/model/logininforModel';
import {R, TableDataInfo} from '@/api/model/resultModel';

// 查询登录日志列表
export function list(query) {
  return request.get<TableDataInfo<SysLogininfor>>({
    url: '/monitor/logininfor/list',
    params: query,
  });
}

// 删除登录日志
export function delLogininfor(infoId) {
  return request.delete<R<void>>({
    url: `/monitor/logininfor/${infoId}`,
  });
}

// 解锁用户登录状态
export function unlockLogininfor(userName) {
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
