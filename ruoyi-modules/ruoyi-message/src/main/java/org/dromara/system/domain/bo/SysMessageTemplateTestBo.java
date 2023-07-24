package org.dromara.system.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 消息模板测试
 *
 * @author hexm
 * @date 2023/06/29 15:05
 */
@Data
public class SysMessageTemplateTestBo implements Serializable {

    /**
     * 消息模板id
     */
    @NotNull(message = "消息模板id不能为空")
    private Long messageTemplateId;

    /**
     * 发送账号
     */
    @NotBlank(message = "发送账号不能为空")
    private String account;

    /**
     * 发送变量
     */
    private Map<String, Object> vars;
}
