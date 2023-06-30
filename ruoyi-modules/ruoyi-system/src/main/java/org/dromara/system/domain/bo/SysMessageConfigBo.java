package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.system.domain.SysMessageConfig;

/**
 * 消息配置业务对象 sys_message_config
 *
 * @author hexm
 * @date 2023-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysMessageConfig.class, reverseConvertGenerate = false)
public class SysMessageConfigBo extends BaseEntity {

    /**
     * 消息设置id
     */
    @NotNull(message = "消息设置id不能为空", groups = {EditGroup.class})
    private Long messageConfigId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String title;

    /**
     * 消息类型
     */
    @NotBlank(message = "消息类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String messageType;

    /**
     * 支持平台标识
     */
    @NotBlank(message = "支持平台标识不能为空", groups = {AddGroup.class, EditGroup.class})
    private String supplierType;

    /**
     * 配置json
     */
    private String configJson;

    /**
     * 状态（1正常 0停用）
     */
    @NotNull(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}
