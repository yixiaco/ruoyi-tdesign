import { request } from '@/utils/request';
import { SysOssQuery, SysOssVo } from '@/api/system/model/ossModel';
import { R, TableDataInfo } from '@/api/model/resultModel';

// 查询OSS对象存储列表
export function listOss(query: SysOssQuery) {
  return request.get<TableDataInfo<SysOssVo>>({
    url: '/system/oss/list',
    params: query,
  });
}

// 查询OSS对象基于id串
export function listByIds(ossId: number) {
  return request.get<R<Array<SysOssVo>>>({
    url: `/system/oss/listByIds/${ossId}`,
  });
}

// 查询OSS对象基于url串
export function listByUrls(urls: string) {
  return request.get<R<Array<SysOssVo>>>({
    url: `/system/oss/listByUrls`,
    params: { urls: encodeURIComponent(urls) },
  });
}

// 删除OSS对象存储
export function delOss(ossId: number) {
  return request.delete<R<void>>({
    url: `/system/oss/${ossId}`,
  });
}
