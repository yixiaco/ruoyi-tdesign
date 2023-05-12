import { SysUserVo } from '@/api/system/model/userModel';

// 登录返回
export interface LoginModel {
  token: string;
}

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
