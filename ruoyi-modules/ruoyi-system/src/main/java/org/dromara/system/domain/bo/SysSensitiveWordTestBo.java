package org.dromara.system.domain.bo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 敏感词测试
 *
 * @author hexm
 * @date 2024/8/20
 */
@Data
public class SysSensitiveWordTestBo implements Serializable {

    /**
     * 测试文本
     */
    @NotBlank(message = "测试文本不能为空")
    private String text;

    /**
     * 敏感词分类
     */
    private String[] category;
}
