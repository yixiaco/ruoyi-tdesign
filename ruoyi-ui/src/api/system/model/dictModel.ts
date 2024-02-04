import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 字典数据查询对象
 */
export interface SysDictDataQuery extends BaseEntity {
  /** 字典标签 */
  dictLabel?: string;
  /** 字典类型 */
  dictType?: string;
}
/**
 * 字典数据业务对象
 */
export interface SysDictDataForm {
  /** 字典编码 */
  dictCode?: number;
  /** 字典排序 */
  dictSort?: number;
  /** 字典标签 */
  dictLabel?: string;
  /** 字典键值 */
  dictValue?: string;
  /** 字典类型 */
  dictType?: string;
  /** 样式属性（其他样式扩展） */
  cssClass?: string;
  /** 表格回显样式 */
  listClass?: string;
  /** 回显风格 */
  tagStyle?: string;
  /** 是否默认（Y是 N否） */
  isDefault?: string;
  /** 创建部门 */
  createDept?: number;
  /** 备注 */
  remark?: string;
}
/**
 * 字典数据视图对象
 */
export interface SysDictDataVo {
  /** 字典编码 */
  dictCode?: number;
  /** 字典排序 */
  dictSort?: number;
  /** 字典标签 */
  dictLabel?: string;
  /** 字典键值 */
  dictValue?: string;
  /** 字典类型 */
  dictType?: string;
  /** 样式属性（其他样式扩展） */
  cssClass?: string;
  /** 表格回显样式 */
  listClass?: 'default' | 'warning' | 'danger' | 'success' | 'primary' | 'text';
  /** 回显风格 */
  tagStyle?: 'light' | 'outline' | 'dark' | 'light-outline';
  /** 是否默认（Y是 N否） */
  isDefault?: string;
  /** 更新时间 */
  updateTime?: any;
  /** 创建时间 */
  createTime?: any;
  /** 备注 */
  remark?: string;
}

/**
 * 字典类型查询对象
 */
export interface SysDictTypeQuery extends BaseEntity {
  /** 字典名称 */
  dictName?: string;
  /** 字典类型 */
  dictType?: string;
}
/**
 * 字典类型业务对象
 */
export interface SysDictTypeForm {
  /** 字典主键 */
  dictId?: number;
  /** 字典名称 */
  dictName?: string;
  /** 字典类型 */
  dictType?: string;
  /** 创建部门 */
  createDept?: number;
  /** 备注 */
  remark?: string;
}
/**
 * 字典类型视图对象
 */
export interface SysDictTypeVo {
  /** 字典主键 */
  dictId?: number;
  /** 字典名称 */
  dictName?: string;
  /** 字典类型 */
  dictType?: string;
  /** 创建时间 */
  createTime?: any;
  /** 更新时间 */
  updateTime?: any;
  /** 备注 */
  remark?: string;
}
