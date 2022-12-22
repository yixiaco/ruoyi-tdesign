/**
 * oss配置
 */
export interface SysOssConfigVo {
  /**
   * 主建
   */
  ossConfigId: number;

  /**
   * 配置key
   */
  configKey: string;

  /**
   * accessKey
   */
  accessKey: string;

  /**
   * 秘钥
   */
  secretKey: string;

  /**
   * 桶名称
   */
  bucketName: string;

  /**
   * 前缀
   */
  prefix: string;

  /**
   * 访问站点
   */
  endpoint: string;

  /**
   * 自定义域名
   */
  domain: string;

  /**
   * 是否https（Y=是,N=否）
   */
  isHttps: string;

  /**
   * 域
   */
  region: string;

  /**
   * 状态（0=正常,1=停用）
   */
  status: string;

  /**
   * 扩展字段
   */
  ext1: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 桶权限类型(01public 2custom)
   */
  accessPolicy: string;
}
