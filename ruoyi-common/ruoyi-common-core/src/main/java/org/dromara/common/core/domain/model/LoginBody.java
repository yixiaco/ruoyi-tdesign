package org.dromara.common.core.domain.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.dromara.common.core.constant.GrantTypeConstants;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录对象
 *
 * @author Lion Li
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "grantType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EmailLoginBody.class, name = GrantTypeConstants.EMAIL),
    @JsonSubTypes.Type(value = PasswordLoginBody.class, name = GrantTypeConstants.PASSWORD),
    @JsonSubTypes.Type(value = SocialLoginBody.class, name = GrantTypeConstants.SOCIAL),
    @JsonSubTypes.Type(value = XcxLoginBody.class, name = GrantTypeConstants.XCX),
    @JsonSubTypes.Type(value = SmsLoginBody.class, name = GrantTypeConstants.SMS),
})
public class LoginBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 客户端id
     */
    @NotBlank(message = "{auth.clientid.not.blank}")
    private String clientId;

    /**
     * 授权类型
     */
    @NotBlank(message = "{auth.grant.type.not.blank}")
    private String grantType;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;

}
