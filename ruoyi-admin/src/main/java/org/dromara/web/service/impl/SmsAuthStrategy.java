package org.dromara.web.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.Constants;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.core.constant.GrantTypeConstants;
import org.dromara.common.core.domain.model.LoginUser;
import org.dromara.common.core.domain.model.SmsLoginBody;
import org.dromara.common.core.enums.LoginType;
import org.dromara.common.core.enums.UserStatus;
import org.dromara.common.core.exception.user.CaptchaExpireException;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.core.utils.MessageUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.satoken.utils.MultipleStpUtil;
import org.dromara.common.tenant.annotation.IgnoreTenant;
import org.dromara.system.domain.SysClient;
import org.dromara.system.domain.SysUser;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.mapper.SysUserMapper;
import org.dromara.web.domain.vo.LoginVo;
import org.dromara.web.service.IAuthStrategy;
import org.dromara.web.service.SysLoginService;
import org.springframework.stereotype.Service;

/**
 * 短信认证策略
 *
 * @author Michelle.Chung
 */
@Slf4j
@Service(GrantTypeConstants.SMS + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class SmsAuthStrategy implements IAuthStrategy<SmsLoginBody> {

    private final SysLoginService loginService;
    private final SysUserMapper userMapper;

    @Override
    @IgnoreTenant
    public LoginVo login(SmsLoginBody loginBody, SysClient client) {
        String phonenumber = loginBody.getPhonenumber();
        String smsCode = loginBody.getSmsCode();
        String clientId = client.getClientId();

        // 通过手机号查找用户
        SysUserVo user = loadUserByPhonenumber(phonenumber);
        String tenantId = user.getTenantId();

        loginService.checkLogin(LoginType.SMS, tenantId, user.getUserName(), () -> !validateSmsCode(tenantId, phonenumber, smsCode));
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        LoginUser loginUser = loginService.buildLoginUser(user);
        loginUser.setClientKey(client.getClientKey());
        loginUser.setDeviceType(client.getDeviceType());
        SaLoginModel model = new SaLoginModel();
        model.setDevice(client.getDeviceType());
        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
        if (client.getTimeout() != null) {
            model.setTimeout(client.getTimeout());
        }
        if (client.getActiveTimeout() != null) {
            model.setActiveTimeout(client.getActiveTimeout());
        }
        // 生成token
        LoginHelper.login(loginUser, model);

        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(MultipleStpUtil.SYSTEM.getTokenValue());
        loginVo.setExpireIn(ObjectUtil.defaultIfNull(client.getTimeout(), MultipleStpUtil.SYSTEM.getTokenTimeout()));
        loginVo.setClientId(clientId);
        return loginVo;
    }

    /**
     * 校验短信验证码
     */
    private boolean validateSmsCode(String tenantId, String phonenumber, String smsCode) {
        String code = RedisUtils.getObject(GlobalConstants.CAPTCHA_CODE_KEY + phonenumber);
        if (StringUtils.isBlank(code)) {
            loginService.recordLogininfor(tenantId, phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        return code.equals(smsCode);
    }

    private SysUserVo loadUserByPhonenumber(String phonenumber) {
        userMapper.selectUserByPhonenumber(phonenumber);
        SysUser user = userMapper.selectOneByQuery(QueryWrapper.create()
            .select(SysUser::getPhonenumber, SysUser::getStatus)
//            .eq(TenantHelper.isEnable(), SysUser::getTenantId, tenantId)
            .eq(SysUser::getPhonenumber, phonenumber));
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", phonenumber);
            throw new UserException("user.not.exists", phonenumber);
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", phonenumber);
            throw new UserException("user.blocked", phonenumber);
        }
        return userMapper.selectUserByPhonenumber(phonenumber);
    }

}
