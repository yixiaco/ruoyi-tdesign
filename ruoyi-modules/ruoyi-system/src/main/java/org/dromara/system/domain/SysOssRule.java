package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * OSS处理规则对象 sys_oss_rule
 *
 * @author hexm
 * @date 2023-05-05
 */
@Data
@TableName("sys_oss_rule")
public class SysOssRule implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * oss规则id
     */
    @TableId(value = "oss_rule_id")
    private Long ossRuleId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 规则名称（例如：80x80，则字段名称将输出字段名_80x80）
     */
    private String ruleName;

    /**
     * 匹配域名
     */
    private String domain;

    /**
     * 媒体类型（规则对匹配的媒体类型生效）
     */
    private String mimeType;

    /**
     * 规则
     */
    private String rule;

    /**
     * 是否覆盖默认字段值
     */
    private String isOverwrite;

    /**
     * 是否默认（不指定规则时，默认输出的规则）
     */
    private String isDefault;

    /**
     * 启用状态
     */
    private String status;

    /**
     * 规则顺序
     */
    private Integer ruleSort;

    /**
     * 创建部门
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createDept;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
