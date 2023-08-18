package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.enums.UserType;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.system.domain.SysOss;

/**
 * OSS对象存储业务对象 sys_oss
 *
 * @author yixiacoco
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysOss.class, reverseConvertGenerate = false)
public class SysOssBo extends BaseEntity {

    /**
     * 对象存储主键
     */
    @NotNull(message = "对象存储主键不能为空", groups = {EditGroup.class})
    private Long ossId;

    /**
     * 原名
     */
    @NotBlank(message = "原名不能为空", groups = {EditGroup.class})
    private String originalName;

    /**
     * 分类id
     */
    @NotNull(message = "分类id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long ossCategoryId = 0L;

    /**
     * 用户类型
     */
    private UserType userType;

    /**
     * 是否锁定状态
     */
    @NotNull(message = "是否锁定状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer isLock = 0;

    /**
     * 是否显示在列表
     */
    private Integer isList = 1;

    /**
     * 上传人
     */
    private Long createBy;

}
