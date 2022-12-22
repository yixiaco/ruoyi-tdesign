import { request } from '@/utils/request';
import { SysPost } from '@/api/system/model/postModel';
import { R, TableDataInfo } from '@/api/model/resultModel';

// 查询岗位列表
export function listPost(query) {
  return request.get<TableDataInfo<SysPost>>({
    url: '/system/post/list',
    params: query,
  });
}

// 查询岗位详细
export function getPost(postId) {
  return request.get<R<SysPost>>({
    url: `/system/post/${postId}`,
  });
}

// 新增岗位
export function addPost(data) {
  return request.post<R<void>>({
    url: '/system/post',
    data,
  });
}

// 修改岗位
export function updatePost(data) {
  return request.put<R<void>>({
    url: '/system/post',
    data,
  });
}

// 删除岗位
export function delPost(postId) {
  return request.delete<R<void>>({
    url: `/system/post/${postId}`,
  });
}
