import { request } from '@/utils/request';

// 查询调度日志列表
export function listJobLog(query: any) {
  return request.get({
    url: '/monitor/jobLog/list',
    params: query,
  });
}

// 删除调度日志
export function delJobLog(jobLogId: any) {
  return request.delete({
    url: `/monitor/jobLog/${jobLogId}`,
  });
}

// 清空调度日志
export function cleanJobLog() {
  return request.delete({
    url: '/monitor/jobLog/clean',
  });
}
