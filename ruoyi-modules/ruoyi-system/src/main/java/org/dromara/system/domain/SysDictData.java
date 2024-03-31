package org.dromara.system.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.enums.YesNoEnum;
import org.dromara.common.mybatis.annotation.ColumnInsert;
import org.dromara.common.mybatis.annotation.ColumnInsertOrUpdate;
import org.dromara.common.mybatis.enums.DateType;
import org.dromara.common.tenant.core.TenantEntity;

import java.util.Date;

/**
 * 字典数据表 sys_dict_data
 *
 * @author Lion Li
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_dict_data")
public class SysDictData extends TenantEntity {

    /**
     * 字典编码
     */
    @Id
    private Long dictCode;

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String cssClass;

    /**
     * 表格字典样式
     */
    private String listClass;

    /**
     * 回显风格
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String tagStyle;

    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建部门
     */
    @ColumnInsert(dateType = DateType.DEPT_ID)
    private Long createDept;

    /**
     * 创建者
     */
    @ColumnInsert(dateType = DateType.USER_ID)
    private Long createBy;

    /**
     * 创建时间
     */
    @ColumnInsert(dateType = DateType.DATE)
    private Date createTime;

    /**
     * 更新者
     */
    @ColumnInsertOrUpdate(dateType = DateType.USER_ID)
    private Long updateBy;

    /**
     * 更新时间
     */
    @ColumnInsertOrUpdate(dateType = DateType.DATE)
    private Date updateTime;

    public boolean getDefault() {
        return YesNoEnum.YES.getCodeStr().equals(this.isDefault);
    }

}
