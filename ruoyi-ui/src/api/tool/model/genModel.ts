import type { BaseEntity } from '@/api/model/resultModel';

/**
 * 代码生成业务查询对象
 */
export interface GenTableQuery extends BaseEntity {
  /** 数据源名称 */
  dataName?: string;
  /** 表名称 */
  tableName?: string;
  /** 表描述 */
  tableComment?: string;
}
/**
 * 修改表名业务对象
 */
export interface GenUpdateTableNameForm {
  /** 编号 */
  tableId?: number;
  /** 数据源名称 */
  dataName?: string;
  /** 表名称 */
  tableName?: string;
}
/**
 * 代码生成业务业务对象
 */
export interface GenTableForm {
  /** 编号 */
  tableId?: number;
  /** 数据源名称 */
  dataName?: string;
  /** 表名称 */
  tableName?: string;
  /** 表描述 */
  tableComment?: string;
  /** 实体类名称 */
  className?: string;
  /** 使用的模板（crud单表操作 tree树表操作） */
  tplCategory?: string;
  /** 生成包路径 */
  packageName?: string;
  /** 生成模块名 */
  moduleName?: string;
  /** 生成业务名 */
  businessName?: string;
  /** 生成功能名 */
  functionName?: string;
  /** 生成功能作者 */
  functionAuthor?: string;
  /** 生成代码方式（0zip压缩包 1自定义路径） */
  genType?: string;
  /** 生成路径（不填默认项目路径） */
  genPath?: string;
  /** 其它生成选项 */
  options?: string;
  /** 备注 */
  remark?: string;
  /** 表列信息 */
  columns?: Array<GenTableColumn>;
  /** 代码生成选项 */
  tableOptions?: GenTableOptions;
}
/**
 * 代码生成业务视图对象
 */
export interface GenTableVo {
  /** 编号 */
  tableId?: number;
  /** 数据源名称 */
  dataName?: string;
  /** 表名称 */
  tableName?: string;
  /** 表描述 */
  tableComment?: string;
  /** 实体类名称 */
  className?: string;
  /** 使用的模板（crud单表操作 tree树表操作） */
  tplCategory?: string;
  /** 生成包路径 */
  packageName?: string;
  /** 生成模块名 */
  moduleName?: string;
  /** 生成业务名 */
  businessName?: string;
  /** 生成功能名 */
  functionName?: string;
  /** 生成功能作者 */
  functionAuthor?: string;
  /** 生成代码方式（0zip压缩包 1自定义路径） */
  genType?: string;
  /** 生成路径（不填默认项目路径） */
  genPath?: string;
  /** 其它生成选项 */
  options?: string;
  /** 更新者 */
  updateBy?: string;
  /** 创建者 */
  createBy?: string;
  /** 创建时间 */
  createTime?: any;
  /** 更新时间 */
  updateTime?: any;
  /** 备注 */
  remark?: string;
  /** 主键信息 */
  pkColumn?: GenTableColumn;
  /** 表列信息 */
  columns?: GenTableColumn[];
  /** 菜单id列表 */
  menuIds?: number[];
  /** 代码生成选项 */
  tableOptions?: GenTableOptions;
}
/**
 * 代码生成业务字段
 */
export interface GenTableColumn extends BaseEntity {
  /** 编号 */
  columnId?: number;
  /** 归属表编号 */
  tableId?: number;
  /** 列名称 */
  columnName?: string;
  /** 列描述 */
  columnComment?: string;
  /** 列类型 */
  columnType?: string;
  /** JAVA类型 */
  javaType?: string;
  /** JAVA字段名 */
  javaField?: string;
  /** 是否主键（1是） */
  isPk?: string;
  /** 是否自增（1是） */
  isIncrement?: string;
  /** 是否必填（1是） */
  isRequired?: string;
  /** 是否为插入字段（1是） */
  isInsert?: string;
  /** 是否编辑字段（1是） */
  isEdit?: string;
  /** 是否列表字段（1是） */
  isList?: string;
  /** 是否查询字段（1是） */
  isQuery?: string;
  /** 是否详情字段（1是） */
  isDetail?: string;
  /** 是否排序字段（1是） */
  isSort?: string;
  /** 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围） */
  queryType?: string;
  /** 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件） */
  htmlType?: string;
  /** 字典类型 */
  dictType?: string;
  /** 排序 */
  sort?: number;
  /** 创建者 */
  createBy?: string;
  /** 创建时间 */
  createTime?: object;
  /** 更新者 */
  updateBy?: string;
  /** 更新时间 */
  updateTime?: object;
}

/**
 * 代码生成选项
 */
export interface GenTableOptions {
  /** 树编码字段 */
  treeCode?: string;
  /** 树父编码字段 */
  treeParentCode?: string;
  /** 树名称字段 */
  treeName?: string;
  /** 上级菜单ID字段 */
  parentMenuId?: number;
  /** 是否使用query对象 */
  isUseQuery?: boolean;
  /** 是否使用bo对象 */
  isUseBO?: boolean;
  /** 是否使用vo对象 */
  isUseVO?: boolean;
  /** 是否使用controller对象 */
  isUseController?: boolean;
  /** 是否生成vue文件 */
  isUseVue?: boolean;
  /** 是否生成sql文件 */
  isUseSql?: boolean;
  /** 菜单icon */
  menuIcon?: string;
  /** 是否使用新增方法 */
  isUseAddMethod?: boolean;
  /** 是否使用修改方法 */
  isUseEditMethod?: boolean;
  /** 是否使用删除方法 */
  isUseRemoveMethod?: boolean;
  /** 是否使用导出方法 */
  isUseExportMethod?: boolean;
  /** 是否使用详情方法 */
  isUseDetailMethod?: boolean;
  /** 是否使用查询方法 */
  isUseQueryMethod?: boolean;
}

export interface GenTableInfo {
  info?: GenTableVo;
  rows?: Array<GenTableColumn>;
}
