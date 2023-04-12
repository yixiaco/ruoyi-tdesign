import { BaseEntity } from '@/api/model/resultModel';

/**
 * 对象存储配置查询对象
 */
export interface SysOssConfigQuery extends BaseEntity {
  /** 配置key */
  configKey?: string;
  /** 桶名称 */
  bucketName?: string;
  /** 是否默认 */
  status?: string;
}

export interface SysOssConfigForm extends BaseEntity {
  /** 主建 */
  ossConfigId?: number;
  /** 配置key */
  configKey?: string;
  /** accessKey */
  accessKey?: string;
  /** 秘钥 */
  secretKey?: string;
  /** 桶名称 */
  bucketName?: string;
  /** 前缀 */
  prefix?: string;
  /** 访问站点 */
  endpoint?: string;
  /** 自定义域名 */
  domain?: string;
  /** 是否https（Y=是,N=否） */
  isHttps?: string;
  /** 是否默认 */
  status?: string;
  /** 域 */
  region?: string;
  /** 扩展字段 */
  ext1?: string;
  /** 备注 */
  remark?: string;
  /** 桶权限类型(0private 1public 2custom) */
  accessPolicy?: string;
  /** 创建者 */
  createBy?: string;
  /** 创建时间 */
  createTime?: string;
  /** 更新者 */
  updateBy?: string;
  /** 更新时间 */
  updateTime?: string;
}

/**
 * oss配置
 */
export interface SysOssConfigVo {
  /** 主建 */
  ossConfigId?: number;
  /** 配置key */
  configKey?: string;
  /** accessKey */
  accessKey?: string;
  /** 秘钥 */
  secretKey?: string;
  /** 桶名称 */
  bucketName?: string;
  /** 前缀 */
  prefix?: string;
  /** 访问站点 */
  endpoint?: string;
  /** 自定义域名 */
  domain?: string;
  /** 是否https（Y=是,N=否） */
  isHttps?: string;
  /** 域 */
  region?: string;
  /** 是否默认（0=是,1=否） */
  status?: string;
  /** 扩展字段 */
  ext1?: string;
  /** 备注 */
  remark?: string;
  /** 桶权限类型(01public 2custom) */
  accessPolicy?: string;
}
