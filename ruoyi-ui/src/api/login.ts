import { request } from '@/utils/request';
import { CaptchaImage, LoginModel, UserInfo } from '@/api/model/loginModel';
import { R } from '@/api/model/ResultModel';

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid,
  };
  return request.post<R<LoginModel>>(
    {
      url: '/login',
      data,
    },
    {
      withToken: false,
    },
  );
}

// 注册方法
export function register(data) {
  return request.post<R<void>>(
    {
      url: '/register',
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
    url: '/getInfo',
  });
}

// 退出方法
export function logout() {
  return request.post<R<void>>({
    url: '/logout',
  });
}

// 获取验证码
export function getCodeImg() {
  return request.get<R<CaptchaImage>>(
    {
      url: '/captchaImage',
      timeout: 20000,
    },
    {
      withToken: false,
    },
  );
}
