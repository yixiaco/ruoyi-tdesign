import { request } from '@/utils/request';
import { R, TableDataInfo } from '@/api/model/resultModel';
import { DeptTreeSelect, SysRole } from '@/api/system/model/roleModel';
import { SysUser } from '@/api/system/model/userModel';

// 查询角色列表
export function listRole(query) {
  return request.get<TableDataInfo<SysRole>>({
    url: '/system/role/list',
    params: query,
  });
}

// 查询角色详细
export function getRole(roleId) {
  return request.get<R<SysRole>>({
    url: `/system/role/${roleId}`,
  });
}

// 新增角色
export function addRole(data) {
  return request.post<R<void>>({
    url: '/system/role',
    data,
  });
}

// 修改角色
export function updateRole(data) {
  return request.put<R<void>>({
    url: '/system/role',
    data,
  });
}

// 角色数据权限
export function dataScope(data) {
  return request.put<R<void>>({
    url: '/system/role/dataScope',
    data,
  });
}

// 角色状态修改
export function changeRoleStatus(roleId, status) {
  const data = {
    roleId,
    status,
  };
  return request.put<R<void>>({
    url: '/system/role/changeStatus',
    data,
  });
}

// 删除角色
export function delRole(roleId) {
  return request.delete<R<void>>({
    url: `/system/role/${roleId}`,
  });
}

// 查询角色已授权用户列表
export function allocatedUserList(query) {
  return request.get<TableDataInfo<SysUser>>({
    url: '/system/role/authUser/allocatedList',
    params: query,
  });
}

// 查询角色未授权用户列表
export function unallocatedUserList(query) {
  return request.get<TableDataInfo<SysUser>>({
    url: '/system/role/authUser/unallocatedList',
    params: query,
  });
}

// 取消用户授权角色
export function authUserCancel(data) {
  return request.put<R<void>>({
    url: '/system/role/authUser/cancel',
    data,
  });
}

// 批量取消用户授权角色
export function authUserCancelAll(data) {
  return request.put<R<void>>({
    url: '/system/role/authUser/cancelAll',
    params: data,
  });
}

// 授权用户选择
export function authUserSelectAll(data) {
  return request.put<R<void>>({
    url: '/system/role/authUser/selectAll',
    params: data,
  });
}

// 根据角色ID查询部门树结构
export function deptTreeSelect(roleId) {
  return request.get<R<DeptTreeSelect>>({
    url: `/system/role/deptTree/${roleId}`,
  });
}
