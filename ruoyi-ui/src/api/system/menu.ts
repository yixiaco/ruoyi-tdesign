import { request } from '@/utils/request';
import { RoleMenuTreeselect, SysMenu } from '@/api/system/model/menuModel';
import { R, TreeModel } from '@/api/model/resultModel';

// 查询菜单列表
export function listMenu(query?) {
  return request.get<R<Array<SysMenu>>>({
    url: '/system/menu/list',
    params: query,
  });
}

// 查询菜单详细
export function getMenu(menuId) {
  return request.get<R<SysMenu>>({
    url: `/system/menu/${menuId}`,
  });
}

// 查询菜单下拉树结构
export function treeselect() {
  return request.get<R<Array<TreeModel<number>>>>({
    url: '/system/menu/treeselect',
  });
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
  return request.get<R<RoleMenuTreeselect>>({
    url: `/system/menu/roleMenuTreeselect/${roleId}`,
  });
}

// 新增菜单
export function addMenu(data) {
  return request.post<R<void>>({
    url: '/system/menu',
    data,
  });
}

// 修改菜单
export function updateMenu(data) {
  return request.put<R<void>>({
    url: '/system/menu',
    data,
  });
}

// 删除菜单
export function delMenu(menuId) {
  return request.delete<R<void>>({
    url: `/system/menu/${menuId}`,
  });
}
