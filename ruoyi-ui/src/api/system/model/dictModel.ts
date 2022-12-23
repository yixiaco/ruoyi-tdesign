import { BaseEntity } from '@/api/model/resultModel';

/**
 * 字典数据
 */
export interface SysDictData extends BaseEntity {
  /**
   * 字典编码
   */
  dictCode: number;

  /**
   * 字典排序
   */
  dictSort: number;

  /**
   * 字典标签
   */
  dictLabel: string;

  /**
   * 字典键值
   */
  dictValue: string;

  /**
   * 字典类型
   */
  dictType: string;

  /**
   * 样式属性（其他样式扩展）
   */
  cssClass: string;

  /**
   * 表格字典样式
   */
  listClass: string;

  /**
   * 是否默认（Y是 N否）
   */
  isDefault: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;

  /**
   * 创建者
   */
  createBy?: string;

  /**
   * 创建时间
   */
  createTime: object;

  /**
   * 更新者
   */
  updateBy?: string;

  /**
   * 更新时间
   */
  updateTime?: object;
}

/**
 * 字典类型
 */
export interface SysDictType extends BaseEntity {
  /**
   * 字典主键
   */
  dictId: number;

  /**
   * 字典名称
   */
  dictName: string;

  /**
   * 字典类型
   */
  dictType: string;

  /**
   * 状态（0正常 1停用）
   */
  status: string;

  /**
   * 备注
   */
  remark: string;
}
