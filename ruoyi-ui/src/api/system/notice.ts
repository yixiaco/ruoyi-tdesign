import { request } from '@/utils/request';
import { R, TableDataInfo } from '@/api/model/resultModel';
import { SysNotice } from '@/api/system/model/noticeModel';

// 查询公告列表
export function listNotice(query) {
  return request.get<TableDataInfo<SysNotice>>({
    url: '/system/notice/list',
    params: query,
  });
}

// 查询公告详细
export function getNotice(noticeId) {
  return request.get<R<SysNotice>>({
    url: `/system/notice/${noticeId}`,
  });
}

// 新增公告
export function addNotice(data) {
  return request.post<R<void>>({
    url: '/system/notice',
    data,
  });
}

// 修改公告
export function updateNotice(data) {
  return request.put<R<void>>({
    url: '/system/notice',
    data,
  });
}

// 删除公告
export function delNotice(noticeId) {
  return request.delete<R<void>>({
    url: `/system/notice/${noticeId}`,
  });
}
