const TokenKey = 'Admin-Token';
import { useUserStore } from '@/store/modules/user';

export function getToken() {
  return localStorage.getItem(TokenKey);
}

export function setToken(token) {
  return localStorage.setItem(TokenKey, token);
}

export function removeToken() {
  return localStorage.removeItem(TokenKey);
}

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
