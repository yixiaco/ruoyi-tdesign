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
 * OSS分类对象 sys_oss_category
 *
 * @author hexm
 * @date 2023-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oss_category")
public class SysOssCategory extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * oss分类id
     */
    @TableId(value = "oss_category_id")
    private Long ossCategoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 父级分类id
     */
    private Long parentId;

    /**
     * 分类路径
     */
    private String categoryPath;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 上传人
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
