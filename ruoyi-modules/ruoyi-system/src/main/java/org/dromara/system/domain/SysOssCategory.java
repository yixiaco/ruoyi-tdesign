package org.dromara.system.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.annotation.ColumnInsert;
import org.dromara.common.mybatis.annotation.ColumnInsertOrUpdate;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.mybatis.enums.DateType;

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
@Table("sys_oss_category")
public class SysOssCategory extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * oss分类id
     */
    @Id
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
    @ColumnInsert(dateType = DateType.USER_ID)
    private Long createBy;

    /**
     * 更新时间
     */
    @ColumnInsertOrUpdate(dateType = DateType.DATE)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ColumnInsert(dateType = DateType.DATE)
    private Date createTime;

}
