import type { SysUserVo } from '@/api/system/model/userModel';

// 登录用户信息
export interface UserInfo {
  user: SysUserVo;
  roles: Array<string>;
  permissions: Array<string>;
}

// 图片验证码
export interface CaptchaImage {
  captchaEnabled: boolean;
  uuid: string;
  img: string;
}

// 登录参数
export interface LoginParam {
  username: string;
  password: string;
  code: string;
  uuid: string;
}

/**
 * 登录请求
 */
export interface LoginData {
  tenantId?: string;
  username?: string;
  password?: string;
  rememberMe?: boolean;
  socialCode?: string;
  socialState?: string;
  source?: string;
  code?: string;
  uuid?: string;
  clientId?: string;
  grantType?: string;
}
/**
 * 登录验证信息
 */
export interface LoginVo {
  /** 授权令牌 */
  access_token?: string;
  /** 刷新令牌 */
  refresh_token?: string;
  /** 授权令牌 access_token 的有效期 */
  expire_in?: number;
  /** 刷新令牌 refresh_token 的有效期 */
  refresh_expire_in?: number;
  /** 应用id */
  client_id?: string;
  /** 令牌权限 */
  scope?: string;
  /** 用户 openid */
  openid?: string;
}

/**
 * 用户登录对象
 */
export interface LoginBody {
  /**
   * 用户名
   */
  username: string;

  /**
   * 用户密码
   */
  password: string;

  /**
   * 验证码
   */
  code: string;

  /**
   * 唯一标识
   */
  uuid: string;
}

/**
 * 用户注册对象
 */
export interface RegisterBody extends LoginBody {
  userType: string;
}
/**
 * 租户列表
 */
export interface TenantListVo {
  tenantId?: string;
  companyName?: string;
  domain?: string;
}
/**
 * 登录租户对象
 */
export interface LoginTenantVo {
  /** 租户开关 */
  tenantEnabled?: boolean;
  /** 租户对象列表 */
  voList?: Array<TenantListVo>;
}
