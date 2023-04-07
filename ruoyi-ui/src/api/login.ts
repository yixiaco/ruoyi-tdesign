import { request } from '@/utils/request';
import { CaptchaImage, LoginModel, RegisterBody, TenantListVo, UserInfo } from '@/api/model/loginModel';
import { R } from '@/api/model/resultModel';

/**
 * 是否登录状态
 */
export function isLogin() {
  return request.get<R<boolean>>({
    url: '/auth/isLogin',
  });
}

// 登录方法
export function login(tenantId: string, username: string, password: string, code: string, uuid: string) {
  const data = {
    tenantId,
    username,
    password,
    code,
    uuid,
  };
  return request.post<R<LoginModel>>(
    {
      url: '/auth/login',
      data,
    },
    {
      withToken: false,
    },
  );
}

// 注册方法
export function register(data: RegisterBody) {
  return request.post<R<void>>(
    {
      url: '/auth/register',
      data,
    },
    {
      withToken: false,
    },
  );
}

// 获取用户详细信息
export function getInfo() {
  return request.get<R<UserInfo>>({
    url: '/system/user/getInfo',
  });
}

// 退出方法
export function logout() {
  return request.post<R<void>>({
    url: '/auth/logout',
  });
}

// 获取验证码
export function getCodeImg() {
  return request.get<R<CaptchaImage>>(
    {
      url: '/code',
      timeout: 20000,
    },
    {
      withToken: false,
    },
  );
}

// 短信验证码
export function getCodeSms() {
  return request.get<R<void>>(
    {
      url: '/sms/code',
      timeout: 20000,
    },
    {
      withToken: false,
    },
  );
}

// 获取租户列表
export function getTenantList() {
  return request.get<R<Array<TenantListVo>>>(
    {
      url: '/auth/tenant/list',
    },
    {
      withToken: false,
    },
  );
}
