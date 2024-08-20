package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 敏感词对象 sys_sensitive_word
 *
 * @author hexm
 * @date 2024-08-16
 */
@Data
@TableName("sys_sensitive_word")
public class SysSensitiveWord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 敏感词id
     */
    @TableId(value = "word_id")
    private Long wordId;

    /**
     * 敏感词
     */
    private String word;

    /**
     * 敏感词类别
     */
    private String category;

    /**
     * 描述
     */
    private String description;

    /**
     * 启用状态
     */
    private Integer status;

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
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
