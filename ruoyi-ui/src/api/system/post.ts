import { request } from '@/utils/request';
import { SysPost } from '@/api/system/model/postModel';
import { R, TableDataInfo } from '@/api/model/resultModel';

// 查询岗位列表
export function listPost(query: SysPost) {
  return request.get<TableDataInfo<SysPost>>({
    url: '/system/post/list',
    params: query,
  });
}

// 查询岗位详细
export function getPost(postId: number) {
  return request.get<R<SysPost>>({
    url: `/system/post/${postId}`,
  });
}

// 新增岗位
export function addPost(data: SysPost) {
  return request.post<R<void>>({
    url: '/system/post',
    data,
  });
}

// 修改岗位
export function updatePost(data: SysPost) {
  return request.put<R<void>>({
    url: '/system/post',
    data,
  });
}

// 删除岗位
export function delPost(postId: number) {
  return request.delete<R<void>>({
    url: `/system/post/${postId}`,
  });
}
