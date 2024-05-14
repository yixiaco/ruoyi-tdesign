package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.system.domain.SysTenantApp;

import java.io.Serial;
import java.io.Serializable;

/**
 * 应用管理业务对象 sys_app
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
@Data
@AutoMapper(target = SysTenantApp.class, reverseConvertGenerate = false)
public class SysTenantAppBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
