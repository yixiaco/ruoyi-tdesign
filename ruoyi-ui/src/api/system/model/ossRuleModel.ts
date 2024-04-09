import type { BaseEntity } from '@/api/model/resultModel';

/**
 * OSS处理规则查询对象
 */
export interface SysOssRuleQuery extends BaseEntity {
  /** 规则名称（例如：80x80，则字段名称将输出字段名_80x80） */
  ruleName?: string;
  /** 匹配域名 */
  domain?: string;
  /** 媒体类型（规则对匹配的媒体类型生效） */
  mimeType?: string;
  /** 是否覆盖默认字段值 */
  isOverwrite?: string;
  /** 是否默认（不指定规则时，默认输出的规则） */
  isDefault?: string;
  /** 启用状态 */
  status?: string;
}
/**
 * OSS处理规则业务对象
 */
export interface SysOssRuleForm {
  /** oss规则id */
  ossRuleId?: number;
  /** 规则名称（例如：80x80，则字段名称将输出字段名_80x80） */
  ruleName?: string;
  /** 匹配域名 */
  domain?: string;
  /** 媒体类型（规则对匹配的媒体类型生效） */
  mimeType?: string;
  /** 规则 */
  rule?: string;
  /** 是否覆盖默认字段值 */
  isOverwrite?: string;
  /** 是否默认（不指定规则时，默认输出的规则） */
  isDefault?: string;
  /** 启用状态 */
  status?: string;
  /** 规则顺序 */
  ruleSort?: number;
  /** 备注 */
  remark?: string;
}
/**
 * OSS处理规则视图对象
 */
export interface SysOssRuleVo {
  /** oss规则id */
  ossRuleId?: number;
  /** 规则名称（例如：80x80，则字段名称将输出字段名_80x80） */
  ruleName?: string;
  /** 匹配域名 */
  domain?: string;
  /** 媒体类型（规则对匹配的媒体类型生效） */
  mimeType?: string;
  /** 规则 */
  rule?: string;
  /** 是否覆盖默认字段值 */
  isOverwrite?: string;
  /** 是否默认（不指定规则时，默认输出的规则） */
  isDefault?: string;
  /** 启用状态 */
  status?: string;
  /** 规则顺序 */
  ruleSort?: number;
  /** 创建部门 */
  createDept?: number;
  /** 创建者 */
  createBy?: number;
  /** 创建时间 */
  createTime?: any;
  /** 更新者 */
  updateBy?: number;
  /** 更新时间 */
  updateTime?: any;
  /** 备注 */
  remark?: string;
}
