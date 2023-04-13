import { R, TableDataInfo } from '@/api/model/resultModel';
import { SysConfigForm, SysConfigVo } from '@/api/system/model/configModel';
import { request } from '@/utils/request';

// 查询参数列表
export function listConfig(query: SysConfigForm) {
  return request.get<TableDataInfo<SysConfigVo>>({
    url: '/system/config/list',
    params: query,
  });
}

// 查询参数详细
export function getConfig(configId: number) {
  return request.get<R<SysConfigVo>>({
    url: `/system/config/${configId}`,
  });
}

// 根据参数键名查询参数值
export function getConfigKey(configKey: string) {
  return request.get<R<string>>({
    url: `/system/config/configKey/${configKey}`,
  });
}

// 新增参数配置
export function addConfig(data: SysConfigForm) {
  return request.post<R<void>>({
    url: '/system/config',
    data,
  });
}

// 修改参数配置
export function updateConfig(data: SysConfigForm) {
  return request.put<R<void>>({
    url: '/system/config',
    data,
  });
}

// 修改参数配置
export function updateConfigByKey(key: string, value: string) {
  return request.put<R<void>>({
    url: '/system/config/updateByKey',
    data: {
      configKey: key,
      configValue: value,
    },
  });
}

// 删除参数配置
export function delConfig(configId: number) {
  return request.delete<R<void>>({
    url: `/system/config/${configId}`,
  });
}

// 刷新参数缓存
export function refreshCache() {
  return request.delete<R<void>>({
    url: '/system/config/refreshCache',
  });
}

// 查询BBS网站参数配置详细
export function getConfigByKeys(keys: string) {
  return request.get<R<Record<string, string>>>({
    url: `/system/config/configKeys`,
    params: { keys },
  });
}

// 修改BBS网站参数配置
export function updateConfigs(configs: Record<string, string>) {
  return request.put<R<void>>({
    url: '/system/config/updateConfigs',
    data: configs,
  });
}
