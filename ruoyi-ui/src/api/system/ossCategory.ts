import { request } from '@/utils/request';
import { R } from '@/api/model/resultModel';
import { SysOssCategoryForm, SysOssCategoryQuery, SysOssCategoryVo } from '@/api/system/model/ossCategoryModel';

// 查询OSS分类列表
export function listOssCategory(query?: SysOssCategoryQuery) {
  return request.get<R<Array<SysOssCategoryVo>>>({
    url: '/system/ossCategory/list',
    params: query,
  });
}

// 查询OSS分类详细
export function getOssCategory(ossCategoryId: number) {
  return request.get<R<SysOssCategoryVo>>({
    url: `/system/ossCategory/${ossCategoryId}`,
  });
}

// 新增OSS分类
export function addOssCategory(data: SysOssCategoryForm) {
  return request.post<R<void>>({
    url: '/system/ossCategory',
    data,
  });
}

// 修改OSS分类
export function updateOssCategory(data: SysOssCategoryForm) {
  return request.put<R<void>>({
    url: '/system/ossCategory',
    data,
  });
}

// 删除OSS分类
export function delOssCategory(ossCategoryIds: number | Array<number>) {
  return request.delete<R<void>>({
    url: `/system/ossCategory/${ossCategoryIds}`,
  });
}
