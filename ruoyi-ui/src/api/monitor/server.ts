import { request } from '@/utils/request';

// 获取服务信息
export function getServer() {
  return request.get({
    url: '/monitor/server',
  });
}
