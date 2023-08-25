import type { BaseEntity } from '@/api/model/resultModel';

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
  /** 文件最大字节长度 */
  maxSize?: number;
  /** 内容类型 */
  contentTypes?: string[];
  /** 分类id */
  ossCategoryId?: number;
  /** 是否锁定状态 */
  isLock?: number;
  /** 创建时间 */
  createTime?: any;
  /** 上传人 */
  createByName?: string;
  /** 服务商 */
  service?: string;
  /** 多个文件后缀，忽略大小写 */
  suffixes?: string[];
}
/**
 * OSS对象存储业务对象
 */
export interface SysOssForm {
  /** 对象存储主键 */
  ossId?: number;
  /** 原名 */
  originalName?: string;
  /** 分类id */
  ossCategoryId?: number;
  /** 用户类型 */
  userType?: string;
  /** 是否锁定状态 */
  isLock?: number;
  /** 上传人 */
  createBy?: number;
}
/**
 * OSS对象存储视图对象
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
  /** 内容类型 */
  contentType?: string;
  /** 分类id */
  ossCategoryId?: number;
  /** 分类路径 */
  categoryPath?: string;
  /** 用户类型 */
  userType?: string;
  /** 是否锁定状态 */
  isLock?: number;
  /** 创建部门 */
  createDept?: number;
  /** 创建时间 */
  createTime?: any;
  /** 上传人 */
  createBy?: number;
  /** 上传人名称 */
  createByName?: string;
  /** 更新时间 */
  updateTime?: any;
  /** 更新人 */
  updateBy?: number;
  /** 服务商 */
  service?: string;
}

export interface SysOssActiveVo extends SysOssVo {
  active: boolean;
}

/**
 * 上传对象信息
 */
export interface SysOssUploadVo {
  /** URL地址 */
  url?: string;
  /** 文件名 */
  fileName?: string;
  /** 对象存储主键 */
  ossId?: string;
}
