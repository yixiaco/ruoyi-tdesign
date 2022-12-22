/**
 * oss
 */
export interface SysOssVo {
  /**
   * 对象存储主键
   */
  ossId: number;

  /**
   * 文件名
   */
  fileName: string;

  /**
   * 原名
   */
  originalName: string;

  /**
   * 文件后缀名
   */
  fileSuffix: string;

  /**
   * URL地址
   */
  url: string;

  /**
   * 创建时间
   */
  createTime: object;

  /**
   * 上传人
   */
  createBy: string;

  /**
   * 服务商
   */
  service: string;
}
