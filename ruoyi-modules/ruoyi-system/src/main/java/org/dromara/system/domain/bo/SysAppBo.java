package org.dromara.system.domain.bo;

import org.dromara.system.domain.SysApp;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.Date;

/**
 * 应用管理业务对象 sys_app
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysApp.class, reverseConvertGenerate = false)
public class SysAppBo extends BaseEntity {

    /**
     * 应用id
     */
    @NotNull(message = "应用id不能为空", groups = {EditGroup.class})
    private Long appid;

    /**
     * 应用类型
     */
    @NotBlank(message = "应用类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String appType;

    /**
     * 应用key
     */
    @NotBlank(message = "应用key不能为空", groups = {AddGroup.class, EditGroup.class})
    private String appKey;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String appName;

    /**
     * 备注
     */
    private String remark;

}
