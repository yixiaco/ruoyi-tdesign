import { BaseEntity } from '@/api/model/resultModel';

/**
 * OSS对象存储查询对象
 */
export interface SysOssQuery extends BaseEntity {
  /** 文件名 */
  fileName?: string;
  /** 原名 */
  originalName?: string;
  /** 文件后缀名 */
  fileSuffix?: string;
  /** URL地址 */
  url?: string;
  /** 创建时间 */
  createTime?: any;
  /** 上传人 */
  createByName?: string;
  /** 服务商 */
  service?: string;
}

export interface SysOssForm extends BaseEntity {
  /** 文件名 */
  fileName?: string;
  /** 原名 */
  originalName?: string;
  /** 文件后缀名 */
  fileSuffix?: string;
  /** URL地址 */
  url?: string;
  /** 服务商 */
  service?: string;
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
 * oss
 */
export interface SysOssVo {
  /** 对象存储主键 */
  ossId?: number;
  /** 文件名 */
  fileName?: string;
  /** 原名 */
  originalName?: string;
  /** 文件后缀名 */
  fileSuffix?: string;
  /** URL地址 */
  url?: string;
  /** 字节长度 */
  size?: number;
  /** 创建时间 */
  createTime?: string;
  /** 上传人 */
  createBy?: string;
  /** 服务商 */
  service?: string;
}
