package org.dromara.system.domain.bo;

import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.Date;

/**
 * 消息模板业务对象 sys_message_template
 *
 * @author hexm
 * @date 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysMessageTemplate.class, reverseConvertGenerate = false)
public class SysMessageTemplateBo extends BaseEntity {

    /**
     * 消息模板id
     */
    @NotNull(message = "消息模板id不能为空", groups = {EditGroup.class})
    private Long messageTemplateId;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String templateName;

    /**
     * 消息配置id
     */
    @NotNull(message = "消息配置id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long messageConfigId;

    /**
     * 消息key
     */
    @NotBlank(message = "消息key不能为空", groups = {AddGroup.class, EditGroup.class})
    private String messageKey;

    /**
     * 消息类型
     */
    @NotBlank(message = "消息类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String messageType;

    /**
     * 模板类型
     */
    @NotBlank(message = "模板类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String templateMode;

    /**
     * 签名
     */
    private String signature;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 内容
     */
    private String content;

    /**
     * 输入变量
     */
    private String varsJson;

    /**
     * 状态（1正常 0停用）
     */
    @NotNull(message = "状态（1正常 0停用）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}
