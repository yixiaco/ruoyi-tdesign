package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.system.domain.SysMessageKey;

/**
 * 消息常量业务对象 sys_message_key
 *
 * @author hexm
 * @date 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysMessageKey.class, reverseConvertGenerate = false)
public class SysMessageKeyBo extends BaseEntity {

    /**
     * 消息key主键
     */
    @NotNull(message = "消息key主键不能为空", groups = {EditGroup.class})
    private Long messageKeyId;

    /**
     * 消息名称
     */
    @NotBlank(message = "消息名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 消息key
     */
    @NotBlank(message = "消息key不能为空", groups = {AddGroup.class, EditGroup.class})
    private String messageKey;

    /**
     * 备注
     */
    private String remark;

}
