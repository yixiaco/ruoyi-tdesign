package org.dromara.web.service;


import org.dromara.common.core.domain.model.LoginBody;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.spring.SpringUtils;
import org.dromara.system.domain.SysClient;
import org.dromara.web.domain.vo.LoginVo;

/**
 * 授权策略
 *
 * @author Michelle.Chung
 */
public interface IAuthStrategy<L extends LoginBody> {

    String BASE_NAME = "AuthStrategy";

    /**
     * 登录
     */
    static <L extends LoginBody> LoginVo login(SysClient client, L loginBody) {
        // 授权类型和客户端id
        String grantType = loginBody.getGrantType();
        String beanName = grantType + BASE_NAME;
        if (!SpringUtils.containsBean(beanName)) {
            throw new ServiceException("授权类型不正确!");
        }
        IAuthStrategy<L> instance = SpringUtils.getBean(beanName);
        return instance.login(loginBody, client);
    }

    /**
     * 登录
     */
    LoginVo login(L loginBody, SysClient client);

}
