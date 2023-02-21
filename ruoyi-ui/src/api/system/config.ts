import { request } from '@/utils/request';
import { R, TableDataInfo } from '@/api/model/resultModel';
import { SysConfig } from '@/api/system/model/configModel';

// 查询参数列表
export function listConfig(query) {
  return request.get<TableDataInfo<SysConfig>>({
    url: '/system/config/list',
    params: query,
  });
}

// 查询参数详细
export function getConfig(configId) {
  return request.get<R<SysConfig>>({
    url: `/system/config/${configId}`,
  });
}

// 根据参数键名查询参数值
export function getConfigKey(configKey) {
  return request.get<R<string>>({
    url: `/system/config/configKey/${configKey}`,
  });
}

// 新增参数配置
export function addConfig(data) {
  return request.post<R<void>>({
    url: '/system/config',
    data,
  });
}

// 修改参数配置
export function updateConfig(data) {
  return request.put<R<void>>({
    url: '/system/config',
    data,
  });
}

// 修改参数配置
export function updateConfigByKey(key, value) {
  return request.put<R<void>>({
    url: '/system/config/updateByKey',
    data: {
      configKey: key,
      configValue: value,
    },
  });
}

// 删除参数配置
export function delConfig(configId) {
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
export function updateConfigs(configs) {
  return request.put<R<void>>({
    url: '/system/config/updateConfigs',
    data: configs,
  });
}
