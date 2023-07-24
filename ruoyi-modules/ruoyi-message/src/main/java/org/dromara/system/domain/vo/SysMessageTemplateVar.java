package org.dromara.system.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;

import java.io.Serializable;

/**
 * 模板变量
 *
 * @author hexm
 * @date 2023/7/2
 */
@Data
public class SysMessageTemplateVar implements Serializable {
    /**
     * 变量key
     */
    @NotBlank(message = "变量key不能为空", groups = {AddGroup.class, EditGroup.class})
    private String key;
    /**
     * 变量值
     */
    @NotBlank(message = "变量值不能为空", groups = {AddGroup.class, EditGroup.class})
    private String value;
}
