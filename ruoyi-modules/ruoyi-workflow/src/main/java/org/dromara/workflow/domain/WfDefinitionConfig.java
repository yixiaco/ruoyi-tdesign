package org.dromara.workflow.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 流程定义配置对象 wf_definition_config
 *
 * @author may
 * @date 2024-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_definition_config")
public class WfDefinitionConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 流程定义ID
     */
    private String definitionId;

    /**
     * 流程KEY
     */
    private String processKey;

    /**
     * 流程版本
     */
    private Integer version;

    /**
     * 备注
     */
    private String remark;


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
}
