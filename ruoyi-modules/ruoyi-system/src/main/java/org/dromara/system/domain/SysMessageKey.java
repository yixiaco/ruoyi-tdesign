package org.dromara.system.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 消息常量对象 sys_message_key
 *
 * @author hexm
 * @date 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_message_key")
public class SysMessageKey extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息key主键
     */
    @TableId(value = "message_key_id")
    private Long messageKeyId;

    /**
     * 消息名称
     */
    private String name;

    /**
     * 消息key
     */
    private String messageKey;

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
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

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
