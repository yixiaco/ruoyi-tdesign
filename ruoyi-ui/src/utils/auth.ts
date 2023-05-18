import { useUserStore } from '@/store/modules/user';

/**
 * 是否拥有权限
 * @param permission
 */
export function useHasPermission(permission: Array<string>) {
  const allPermission = '*:*:*';
  const { permissions } = useUserStore();

  if (permission && permission.length > 0) {
    const permissionFlag = permission;
    return permissions.some((permission) => {
      return allPermission === permission || permissionFlag.includes(permission);
    });
  }
  return false;
}

/**
 * 是否拥有角色
 * @param role
 */
export function useHasRole(role: Array<string>) {
  const superAdmin = 'admin';
  const { roles } = useUserStore();

  if (role && role.length > 0) {
    const roleFlag = role;
    return roles.some((role) => {
      return superAdmin === role || roleFlag.includes(role);
    });
  }
  return false;
}
