import { request } from '@/utils/request';
import { R, TableDataInfo } from '@/api/model/resultModel';
import { SysTenantPackageBo, SysTenantPackageVo } from '@/api/system/model/tenantPackageModel';

// 查询租户套餐列表
export function listTenantPackage(query?: SysTenantPackageBo) {
  return request.get<TableDataInfo<SysTenantPackageVo>>({
    url: '/system/tenant/package/list',
    params: query,
  });
}

// 查询租户套餐详细
export function getTenantPackage(packageId: number) {
  return request.get<R<SysTenantPackageVo>>({
    url: `/system/tenant/package/${packageId}`,
  });
}

// 新增租户套餐
export function addTenantPackage(data: SysTenantPackageBo) {
  return request.post({
    url: '/system/tenant/package',
    data,
  });
}

// 修改租户套餐
export function updateTenantPackage(data: SysTenantPackageBo) {
  return request.put({
    url: '/system/tenant/package',
    data,
  });
}

// 租户套餐状态修改
export function changePackageStatus(packageId: number, status: string) {
  const data = {
    packageId,
    status,
  };
  return request.put({
    url: '/system/tenant/package/changeStatus',
    data,
  });
}

// 删除租户套餐
export function delTenantPackage(packageId: string | number) {
  return request.delete({
    url: `/system/tenant/package/${packageId}`,
  });
}
