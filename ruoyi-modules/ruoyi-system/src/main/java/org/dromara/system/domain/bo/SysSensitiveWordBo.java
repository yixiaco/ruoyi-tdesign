package org.dromara.system.domain.bo;

import org.dromara.system.domain.SysSensitiveWord;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.hibernate.validator.constraints.Length;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

import java.util.Date;
import java.io.Serial;
import java.io.Serializable;

/**
 * 敏感词业务对象 sys_sensitive_word
 *
 * @author hexm
 * @date 2024-08-16
 */
@Data
@AutoMapper(target = SysSensitiveWord.class, reverseConvertGenerate = false)
public class SysSensitiveWordBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 敏感词id
     */
    @NotNull(message = "敏感词id不能为空", groups = {EditGroup.class})
    private Long wordId;
    /**
     * 敏感词
     */
    @NotBlank(message = "敏感词不能为空", groups = {AddGroup.class, EditGroup.class})
    @Length(max = 255, message = "敏感词不能大于{max}个字符", groups = {AddGroup.class, EditGroup.class})
    private String word;
    /**
     * 敏感词类别
     */
    @NotBlank(message = "敏感词类别不能为空", groups = {AddGroup.class, EditGroup.class})
    @Length(max = 20, message = "敏感词类别不能大于{max}个字符", groups = {AddGroup.class, EditGroup.class})
    private String category;
    /**
     * 描述
     */
    @Length(max = 500, message = "描述不能大于{max}个字符", groups = {AddGroup.class, EditGroup.class})
    private String description;
    /**
     * 启用状态
     */
    @NotNull(message = "启用状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer status;
}
