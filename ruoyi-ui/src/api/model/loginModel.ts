import { SysUser } from '@/api/system/model/userModel';

// 登录返回
export interface LoginModel {
  token: string;
}

// 登录用户信息
export interface UserInfo {
  user: SysUser;
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
