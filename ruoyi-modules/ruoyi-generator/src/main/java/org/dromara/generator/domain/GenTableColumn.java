package org.dromara.generator.domain;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.util.Date;
import java.util.Objects;

/**
 * 代码生成业务字段表 gen_table_column
 *
 * @author Lion Li
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Table("gen_table_column")
public class GenTableColumn extends BaseEntity {

    /**
     * 编号
     */
    @Id
    private Long columnId;

    /**
     * 归属表编号
     */
    private Long tableId;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列描述
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String columnComment;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * JAVA类型
     */
    private String javaType;

    /**
     * JAVA字段名
     */
    @NotBlank(message = "Java属性不能为空")
    private String javaField;

    /**
     * 是否主键（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isPk;

    /**
     * 是否自增（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isIncrement;

    /**
     * 是否必填（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isRequired;

    /**
     * 是否为插入字段（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isInsert;

    /**
     * 是否编辑字段（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isEdit;

    /**
     * 是否列表字段（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isList;

    /**
     * 是否查询字段（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isQuery;

    /**
     * 是否详情字段（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isDetail;

    /**
     * 是否排序字段（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isSort;

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

    /**
     * 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围）
     */
    private String queryType;

    /**
     * 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件）
     */
    private String htmlType;

    /**
     * 字典类型
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String dictType;

    /**
     * 排序
     */
    private Integer sort;

    public String getCapJavaField() {
        return StringUtils.uncapitalize(javaField);
    }

    public boolean isPk() {
        return isPk != null && StringUtils.equals("1", isPk);
    }

    public boolean isIncrement() {
        return isIncrement != null && Objects.equals("1", isIncrement);
    }

    public boolean isRequired() {
        return isRequired != null && Objects.equals("1", isRequired);
    }

    public boolean isInsert() {
        return isInsert != null && Objects.equals("1", isInsert);
    }

    public boolean isEdit() {
        return isEdit != null && Objects.equals("1", isEdit);
    }

    public boolean isList() {
        return isList != null && Objects.equals("1", isList);
    }

    public boolean isQuery() {
        return isQuery != null && Objects.equals("1", isQuery);
    }

    public boolean isDetail() {
        return isDetail != null && Objects.equals("1", isDetail);
    }

    public boolean isSorting() {
        return isSort != null && Objects.equals("1", isSort) && isList();
    }

    public boolean isSuperColumn() {
        return isSuperColumn(this.javaField);
    }

    public static boolean isSuperColumn(String javaField) {
        return StringUtils.equalsAnyIgnoreCase(javaField,
            // BaseEntity
            "createBy", "createTime", "updateBy", "updateTime",
            // TreeEntity
            "parentName", "parentId");
    }

    public boolean isUsableColumn() {
        return isUsableColumn(javaField);
    }

    public static boolean isUsableColumn(String javaField) {
        // isSuperColumn()中的名单用于避免生成多余Domain属性，若某些属性在生成页面时需要用到不能忽略，则放在此处白名单
        return StringUtils.equalsAnyIgnoreCase(javaField, "parentId", "orderNum", "remark");
    }

    public String readConverterExp() {
        String remarks = StringUtils.substringBetween(this.columnComment, "（", "）");
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(remarks)) {
            for (String value : remarks.split(" ")) {
                if (StringUtils.isNotEmpty(value)) {
                    Object startStr = value.subSequence(0, 1);
                    String endStr = value.substring(1);
                    sb.append(startStr).append("=").append(endStr).append(StringUtils.SEPARATOR);
                }
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
            return this.columnComment;
        }
    }
}
