package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.system.domain.SysOssCategory;

/**
 * OSS分类业务对象 sys_oss_category
 *
 * @author hexm
 * @date 2023-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysOssCategory.class, reverseConvertGenerate = false)
public class SysOssCategoryBo extends BaseEntity {

    /**
     * oss分类id
     */
    @NotNull(message = "oss分类id不能为空", groups = {EditGroup.class})
    private Long ossCategoryId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Pattern(regexp = "^[^/%_*]*$", message = "分类名不能包含下列任何字符：/%_*")
    private String categoryName;

    /**
     * 父级分类id
     */
    @NotNull(message = "父级分类id不能为空", groups = {AddGroup.class, EditGroup.class})
    @Min(value = 0, message = "父级分类id不能小于0")
    private Long parentId;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer orderNum;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 上传人
     */
    private Long createBy;

}
