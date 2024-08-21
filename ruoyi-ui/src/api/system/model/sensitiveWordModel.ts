import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 敏感词查询对象
 */
export interface SysSensitiveWordQuery extends BaseEntity {
  /** 敏感词 */
  word?: string;
  /** 敏感词类别 */
  category?: string;
  /** 启用状态 */
  status?: number;
}
/**
 * 敏感词业务对象
 */
export interface SysSensitiveWordForm {
  /** 敏感词id */
  wordId?: number;
  /** 敏感词 */
  word?: string;
  /** 敏感词类别 */
  category?: string;
  /** 描述 */
  description?: string;
  /** 启用状态 */
  status?: number;
}
/**
 * 敏感词视图对象
 */
export interface SysSensitiveWordVo {
  /** 敏感词id */
  wordId?: number;
  /** 敏感词 */
  word?: string;
  /** 敏感词类别 */
  category?: string;
  /** 描述 */
  description?: string;
  /** 启用状态 */
  status?: number;
  /** 更新时间 */
  updateTime?: any;
  /** 创建时间 */
  createTime?: any;
}

export interface SysSensitiveWordTest {
  /** 测试文本 */
  text: string;
  /** 敏感词类别 */
  category?: string[];
}
/**
 * 敏感词测试结果vo
 */
export interface SysSensitiveWordTestVo {
  /** 是否包含敏感词 */
  containsSensitiveWord: boolean;
  /** 敏感词替换后的内容 */
  sensitiveWordReplace: string;
  /** 包含的敏感词 */
  sensitiveWords: string[];
}
