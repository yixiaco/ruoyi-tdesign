import { request } from '@/utils/request';
import { R, TableDataInfo } from '@/api/model/resultModel';
import { SysNoticeBo, SysNoticeQuery, SysNoticeVo } from '@/api/system/model/noticeModel';

// 查询公告列表
export function listNotice(query: SysNoticeQuery) {
  return request.get<TableDataInfo<SysNoticeVo>>({
    url: '/system/notice/list',
    params: query,
  });
}

// 查询公告详细
export function getNotice(noticeId: number) {
  return request.get<R<SysNoticeVo>>({
    url: `/system/notice/${noticeId}`,
  });
}

// 新增公告
export function addNotice(data: SysNoticeBo) {
  return request.post<R<void>>({
    url: '/system/notice',
    data,
  });
}

// 修改公告
export function updateNotice(data: SysNoticeBo) {
  return request.put<R<void>>({
    url: '/system/notice',
    data,
  });
}

// 删除公告
export function delNotice(noticeId: number) {
  return request.delete<R<void>>({
    url: `/system/notice/${noticeId}`,
  });
}
