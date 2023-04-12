import { request } from '@/utils/request';
import { SysOssConfigForm, SysOssConfigQuery, SysOssConfigVo } from '@/api/system/model/ossConfigModel';
import { R, TableDataInfo } from '@/api/model/resultModel';

// 查询对象存储配置列表
export function listOssConfig(query: SysOssConfigQuery) {
  return request.get<TableDataInfo<SysOssConfigVo>>({
    url: '/system/oss/config/list',
    params: query,
  });
}

// 查询对象存储配置详细
export function getOssConfig(ossConfigId: number) {
  return request.get<R<SysOssConfigVo>>({
    url: `/system/oss/config/${ossConfigId}`,
  });
}

// 新增对象存储配置
export function addOssConfig(data: SysOssConfigForm) {
  return request.post<R<void>>({
    url: '/system/oss/config',
    data,
  });
}

// 修改对象存储配置
export function updateOssConfig(data: SysOssConfigForm) {
  return request.put<R<void>>({
    url: '/system/oss/config',
    data,
  });
}

// 删除对象存储配置
export function delOssConfig(ossConfigId: number) {
  return request.delete<R<void>>({
    url: `/system/oss/config/${ossConfigId}`,
  });
}

// 对象存储状态修改
export function changeOssConfigStatus(ossConfigId: number, status: string, configKey: string) {
  const data = {
    ossConfigId,
    status,
    configKey,
  };
  return request.put<R<void>>({
    url: '/system/oss/config/changeStatus',
    data,
  });
}
