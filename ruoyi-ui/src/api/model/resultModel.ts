/**
 * 返回分页结果
 */
export interface TableDataInfo<T> {
  /**
   * 总记录数
   */
  total: number;

  /**
   * 列表数据
   */
  rows: Array<T>;

  /**
   * 消息状态码
   */
  code: number;

  /**
   * 消息内容
   */
  msg: string;
}

/**
 * 返回结果
 */
export interface R<T> {
  // 消息码
  code: number;
  // 消息
  msg: string;
  // 数据
  data: T;
}

/**
 * 基本类型
 */
export interface BaseEntity {
  /**
   * 搜索值
   */
  searchValue?: string;

  /**
   * 请求参数
   */
  params: Map<string, object>;
}

/**
 * 树结构
 */
export interface TreeEntity<T> extends BaseEntity {
  /**
   * 父菜单名称
   */
  parentName?: string;

  /**
   * 父菜单ID
   */
  parentId?: number;

  /**
   * 子部门
   */
  children: Array<T>;
}

export interface TreeModel<T> {
  id: T;
  parentId?: T;
  label: string;
  weight?: any;
  children?: Array<TreeModel<T>>;
}
