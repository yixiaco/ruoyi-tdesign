/**
 * 社会化关系视图对象
 */
export interface SysSocialVo {
  /** 主键 */
  id?: number;
  /** 用户ID */
  userId?: number;
  /** 平台+平台唯一id */
  authId?: string;
  /** 用户来源 */
  source?: string;
  /** 平台编号唯一id */
  openId?: string;
  /** 登录账号 */
  userName?: string;
  /** 用户昵称 */
  nickName?: string;
  /** 用户邮箱 */
  email?: string;
  /** 头像地址 */
  avatar?: string;
  /** 用户的授权令牌 */
  accessToken?: string;
  /** 用户的授权令牌的有效期，部分平台可能没有 */
  expireIn?: number;
  /** 刷新令牌，部分平台可能没有 */
  refreshToken?: string;
  /** 平台的授权信息，部分平台可能没有 */
  accessCode?: string;
  /** 用户的 unionid */
  unionId?: string;
  /** 授予的权限，部分平台可能没有 */
  scope?: string;
  /** 个别平台的授权信息，部分平台可能没有 */
  tokenType?: string;
  /** id token，部分平台可能没有 */
  idToken?: string;
  /** 小米平台用户的附带属性，部分平台可能没有 */
  macAlgorithm?: string;
  /** 小米平台用户的附带属性，部分平台可能没有 */
  macKey?: string;
  /** 用户的授权code，部分平台可能没有 */
  code?: string;
  /** Twitter平台用户的附带属性，部分平台可能没有 */
  oauthToken?: string;
  /** Twitter平台用户的附带属性，部分平台可能没有 */
  oauthTokenSecret?: string;
  /** 创建时间 */
  createTime?: any;
  /** 更新时间 */
  updateTime?: any;
}
