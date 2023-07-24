package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.system.domain.vo.SysMessageTemplateVar;

import java.util.List;

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
    @NotNull(message = "消息配置不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long messageConfigId;

    /**
     * 消息key主键
     */
    @NotNull(message = "消息key不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long messageKeyId;

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
     * 标题
     */
    private String title;

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
    @Valid
    private List<SysMessageTemplateVar> varsList;

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
