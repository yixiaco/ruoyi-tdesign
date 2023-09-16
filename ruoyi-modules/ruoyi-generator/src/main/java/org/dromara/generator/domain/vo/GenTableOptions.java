package org.dromara.generator.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 代码生成选项
 *
 * @author hexm
 * @date 2023/8/6
 */
@Data
public class GenTableOptions implements Serializable {
    /**
     * 树编码字段
     */
    private String treeCode;
    /**
     * 树父编码字段
     */
    private String treeParentCode;
    /**
     * 树名称字段
     */
    private String treeName;
    /**
     * 上级菜单ID字段
     */
    private long parentMenuId = 0L;
    /**
     * 是否使用query对象
     */
    private Boolean isUseQuery = true;
    /**
     * 是否使用bo对象
     */
    private Boolean isUseBO = true;
    /**
     * 是否使用vo对象
     */
    private Boolean isUseVO = true;
    /**
     * 是否使用controller对象
     */
    private Boolean isUseController = true;
    /**
     * 是否生成vue文件
     */
    private Boolean isUseVue = true;
    /**
     * 是否生成sql文件
     */
    private Boolean isUseSql = true;
    /**
     * 菜单icon
     */
    private String menuIcon = "#";
    /**
     * 是否使用新增方法
     */
    private Boolean isUseAddMethod = true;
    /**
     * 是否使用修改方法
     */
    private Boolean isUseEditMethod = true;
    /**
     * 是否使用删除方法
     */
    private Boolean isUseRemoveMethod = true;
    /**
     * 是否使用导出方法
     */
    private Boolean isUseExportMethod = true;
    /**
     * 是否使用详情方法
     */
    private Boolean isUseDetailMethod = true;
    /**
     * 是否使用查询方法
     */
    private Boolean isUseQueryMethod = true;

    public Boolean getIsUseQuery() {
        return isUseQuery == null || isUseQuery;
    }

    public Boolean getIsUseBO() {
        return isUseBO == null || isUseBO;
    }

    public Boolean getIsUseVO() {
        return isUseVO == null || isUseVO;
    }
}
