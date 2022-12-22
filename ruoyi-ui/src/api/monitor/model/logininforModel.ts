/**
 * 系统访问记录
 */
export interface SysLogininfor {
  /**
   * ID
   */
  infoId: number;

  /**
   * 用户账号
   */
  userName: string;

  /**
   * 登录状态 0成功 1失败
   */
  status: string;

  /**
   * 登录IP地址
   */
  ipaddr: string;

  /**
   * 登录地点
   */
  loginLocation: string;

  /**
   * 浏览器类型
   */
  browser: string;

  /**
   * 操作系统
   */
  os: string;

  /**
   * 提示消息
   */
  msg: string;

  /**
   * 访问时间
   */
  loginTime: object;

  /**
   * 请求参数
   */
  params: Map<string, object>;
}
