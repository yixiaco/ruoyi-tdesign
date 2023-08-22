import type { R, TableDataInfo } from '@/api/model/resultModel';
import type { SysUserOnline, SysUserOnlineQuery } from '@/api/monitor/model/userOnlineModel';
import { request } from '@/utils/request';

// 查询在线用户列表
export function list(query: SysUserOnlineQuery) {
  return request.get<TableDataInfo<SysUserOnline>>({
    url: '/monitor/online/list',
    params: query,
  });
}

// 强退用户
export function forceLogout(tokenId: string) {
  return request.delete<R<void>>({
    url: `/monitor/online/${tokenId}`,
  });
}
