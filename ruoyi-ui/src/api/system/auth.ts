import { request } from '@/utils/request';

// 绑定账号
export function authBinding(source: string) {
  return request.get({
    url: `/auth/binding/${source}`,
  });
}

// 解绑账号
export function authUnlock(authId: string) {
  return request.delete({
    url: `/auth/unlock/${authId}`,
  });
}

// 获取授权列表
export function getAuthList() {
  return request.get({
    url: '/system/social/list',
  });
}
