package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;
import java.util.Date;

/**
 * 消息配置对象 sys_message_config
 *
 * @author hexm
 * @date 2023-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_message_config")
public class SysMessageConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息设置id
     */
    @TableId(value = "message_config_id")
    private Long messageConfigId;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 支持平台标识
     */
    private String supplierType;

    /**
     * 配置json
     */
    private String configJson;

    /**
     * 状态（1正常 0停用）
     */
    private Integer status;

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
