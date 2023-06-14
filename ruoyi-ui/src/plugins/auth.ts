import { useUserStore } from '@/store/modules/user';

function authPermission(permission: string) {
  const allPermission = '*:*:*';
  const { permissions } = useUserStore();
  if (permission && permission.length > 0) {
    return permissions.some((v) => {
      return allPermission === v || v === permission;
    });
  }
  return false;
}

function authRole(role: string) {
  const superAdmin = 'admin';
  const { roles } = useUserStore();
  if (role && role.length > 0) {
    return roles.some((v) => {
      return superAdmin === v || v === role;
    });
  }
  return false;
}

export default {
  // 验证用户是否具备某权限
  hasPermi(permission: string) {
    return authPermission(permission);
  },
  // 验证用户是否含有指定权限，只需包含其中一个
  hasPermiOr(permissions: string[]) {
    return permissions.some((item) => {
      return authPermission(item);
    });
  },
  // 验证用户是否含有指定权限，必须全部拥有
  hasPermiAnd(permissions: any[]) {
    return permissions.every((item) => {
      return authPermission(item);
    });
  },
  // 验证用户是否具备某角色
  hasRole(role: string) {
    return authRole(role);
  },
  // 验证用户是否含有指定角色，只需包含其中一个
  hasRoleOr(roles: string[]) {
    return roles.some((item) => {
      return authRole(item);
    });
  },
  // 验证用户是否含有指定角色，必须全部拥有
  hasRoleAnd(roles: string[]) {
    return roles.every((item) => {
      return authRole(item);
    });
  },
};
