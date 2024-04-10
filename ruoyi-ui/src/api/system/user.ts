import type { R, TableDataInfo, TreeModel } from '@/api/model/resultModel';
import type { SysUserForm, SysUserInfoVo, SysUserQuery, SysUserVo, UserAuthRole } from '@/api/system/model/userModel';
import { request } from '@/utils/request';
import { parseStrEmpty } from '@/utils/ruoyi';

// 查询用户列表
export function listUser(query: SysUserQuery) {
  return request.get<TableDataInfo<SysUserVo>>({
    url: '/system/user/list',
    params: query,
  });
}

// 查询用户详细
export function getUser(userId?: number) {
  return request.get<R<SysUserInfoVo>>({
    url: `/system/user/${parseStrEmpty(userId)}`,
  });
}

// 新增用户
export function addUser(data: SysUserForm) {
  return request.post<R<void>>({
    url: '/system/user',
    data,
  });
}

// 修改用户
export function updateUser(data: SysUserForm) {
  return request.put<R<void>>({
    url: '/system/user',
    data,
  });
}

// 删除用户
export function delUser(userId: number | number[]) {
  return request.delete<R<void>>({
    url: `/system/user/${userId}`,
    method: 'delete',
  });
}

// 用户密码重置
export function resetUserPwd(userId: number, password: string) {
  const data = {
    userId,
    password,
  };
  return request.put<R<void>>(
    {
      url: '/system/user/resetPwd',
      data,
    },
    {
      withEncrypt: true,
    },
  );
}

// 用户状态修改
export function changeUserStatus(userId: number, status: string) {
  const data = {
    userId,
    status,
  };
  return request.put<R<void>>({
    url: '/system/user/changeStatus',
    data,
  });
}

// 查询授权角色
export function getAuthRole(userId: number | string) {
  return request.get<R<UserAuthRole>>({
    url: `/system/user/authRole/${userId}`,
  });
}

// 保存授权角色
export function updateAuthRole(data: { userId: number; roleIds: string }) {
  return request.put<R<void>>({
    url: '/system/user/authRole',
    params: data,
  });
}

/**
 * 查询当前部门的所有用户信息
 * @param deptId
 */
export function listUserByDeptId(deptId: number) {
  return request.get<R<SysUserVo[]>>({
    url: `/system/user/list/dept/${deptId}`,
  });
}

// 查询部门下拉树结构
export function deptTreeSelect() {
  return request.get<R<Array<TreeModel<number>>>>({
    url: '/system/user/deptTree',
    method: 'get',
  });
}
