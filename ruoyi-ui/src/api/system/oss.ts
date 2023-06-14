import { R, TableDataInfo } from '@/api/model/resultModel';
import { SysOssQuery, SysOssVo } from '@/api/system/model/ossModel';
import { request } from '@/utils/request';

// 查询OSS对象存储列表
export function listOss(query: SysOssQuery) {
  return request.get<TableDataInfo<SysOssVo>>({
    url: '/resource/oss/list',
    params: query,
  });
}

// 查询OSS对象基于id串
export function listByIds(ossId: string) {
  return request.get<R<Array<SysOssVo>>>({
    url: `/resource/oss/listByIds/${ossId}`,
  });
}

// 查询OSS对象基于url串
export function listByUrls(urls: string) {
  return request.get<R<Array<SysOssVo>>>({
    url: `/resource/oss/listByUrls`,
    params: { urls: encodeURIComponent(urls) },
  });
}

// 删除OSS对象存储
export function delOss(ossId: number | number[]) {
  return request.delete<R<void>>({
    url: `/resource/oss/${ossId}`,
  });
}
