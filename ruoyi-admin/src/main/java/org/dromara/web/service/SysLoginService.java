package org.dromara.web.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.config.UserLoginConfig;
import org.dromara.common.core.constant.Constants;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.core.constant.TenantConstants;
import org.dromara.common.core.domain.dto.RoleDTO;
import org.dromara.common.core.domain.model.LoginUser;
import org.dromara.common.core.domain.model.XcxLoginUser;
import org.dromara.common.core.enums.DeviceType;
import org.dromara.common.core.enums.LoginType;
import org.dromara.common.core.enums.TenantStatus;
import org.dromara.common.core.enums.UserStatus;
import org.dromara.common.core.exception.user.CaptchaException;
import org.dromara.common.core.exception.user.CaptchaExpireException;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.MessageUtils;
import org.dromara.common.core.utils.ServletUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.core.utils.spring.SpringUtils;
import org.dromara.common.log.event.LogininforEvent;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.satoken.utils.MultipleStpUtil;
import org.dromara.common.tenant.exception.TenantException;
import org.dromara.common.tenant.helper.TenantHelper;
import org.dromara.common.web.config.properties.CaptchaProperties;
import org.dromara.system.domain.SysUser;
import org.dromara.system.domain.vo.SysTenantVo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.mapper.SysUserMapper;
import org.dromara.system.service.ISysPermissionService;
import org.dromara.system.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * 登录校验方法
 *
 * @author Lion Li
 */
@Slf4j
@Service
public class SysLoginService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private CaptchaProperties captchaProperties;
    @Autowired
    private UserLoginConfig userLoginConfig;
    @Autowired
    private ISysPermissionService permissionService;
    @Autowired
    private ISysTenantService tenantService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        boolean captchaEnabled = captchaProperties.getEnable();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, code, uuid);
        }

        SysUserVo user = loadUserByUsername(username);

        String tenantId = user.getTenantId();

        // 校验租户
        checkTenant(tenantId);

        checkLogin(LoginType.PASSWORD, tenantId, username, () -> !BCrypt.checkpw(password, user.getPassword()));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);

        recordLogininfor(loginUser.getTenantId(), username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getUserId());
        return MultipleStpUtil.SYSTEM.getTokenValue();
    }

    /**
     * 短信登录
     *
     * @param phonenumber
     * @param smsCode
     * @return
     */
    public String smsLogin(String phonenumber, String smsCode) {
        // 通过手机号查找用户
        SysUserVo user = loadUserByPhonenumber(phonenumber);

        String tenantId = user.getTenantId();

        // 校验租户
        checkTenant(tenantId);

        checkLogin(LoginType.SMS, tenantId, user.getUserName(), () -> !validateSmsCode(tenantId, phonenumber, smsCode));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);

        recordLogininfor(loginUser.getTenantId(), user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getUserId());
        return MultipleStpUtil.SYSTEM.getTokenValue();
    }

    /**
     * 邮箱登录
     *
     * @param email
     * @param emailCode
     * @return
     */
    public String emailLogin(String email, String emailCode) {
        // 通过邮箱查找用户
        SysUserVo user = loadUserByEmail(email);
        String tenantId = user.getTenantId();

        // 校验租户
        checkTenant(tenantId);

        checkLogin(LoginType.EMAIL, tenantId, user.getUserName(), () -> !validateEmailCode(tenantId, email, emailCode));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);

        recordLogininfor(loginUser.getTenantId(), user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getUserId());
        return MultipleStpUtil.SYSTEM.getTokenValue();
    }


    /**
     * 小程序登录
     *
     * @param xcxCode
     * @return
     */
    public String xcxLogin(String xcxCode) {
        // xcxCode 为 小程序调用 wx.login 授权后获取
        // todo 以下自行实现
        // 校验 appid + appsrcret + xcxCode 调用登录凭证校验接口 获取 session_key 与 openid
        String openid = "";
        SysUserVo user = loadUserByOpenid(openid);
        // 校验租户
        checkTenant(user.getTenantId());

        // 此处可根据登录用户的数据不同 自行创建 loginUser
        XcxLoginUser loginUser = new XcxLoginUser();
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUserId(user.getUserId());
        loginUser.setUsername(user.getUserName());
        loginUser.setUserType(user.getUserType());
        loginUser.setOpenId(openid);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.XCX);

        recordLogininfor(loginUser.getTenantId(), user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getUserId());
        return MultipleStpUtil.SYSTEM.getTokenValue();
    }

    /**
     * 退出登录
     */
    public void logout() {
        try {
            LoginUser loginUser = LoginHelper.getUser();
            if (loginUser != null) {
                if (TenantHelper.isEnable() && LoginHelper.isSuperAdmin()) {
                    // 超级管理员 登出清除动态租户
                    TenantHelper.clearDynamic();
                }
                MultipleStpUtil.SYSTEM.logout();
                recordLogininfor(loginUser.getTenantId(), loginUser.getUsername(), Constants.LOGOUT, MessageUtils.message("user.logout.success"));
            }
        } catch (NotLoginException ignored) {
        }
    }

    /**
     * 记录登录信息
     *
     * @param tenantId 租户ID
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    private void recordLogininfor(String tenantId, String username, String status, String message) {
        HttpServletRequest request = ServletUtils.getRequest();
        final UserAgent userAgent = UserAgentUtil.parse(request.getHeader(Header.USER_AGENT.getValue()));
        final String ip = ServletUtils.getClientIP(request);
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();

        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setTenantId(tenantId);
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        logininforEvent.setIp(ip);
        logininforEvent.setOs(os);
        logininforEvent.setBrowser(browser);
        SpringUtils.context().publishEvent(logininforEvent);
    }

    /**
     * 校验短信验证码
     */
    private boolean validateSmsCode(String tenantId, String phonenumber, String smsCode) {
        String code = RedisUtils.getObject(GlobalConstants.CAPTCHA_CODE_KEY + phonenumber);
        if (StringUtils.isBlank(code)) {
            recordLogininfor(tenantId, phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        return code.equals(smsCode);
    }

    /**
     * 校验邮箱验证码
     */
    private boolean validateEmailCode(String tenantId, String email, String emailCode) {
        String code = RedisUtils.getObject(GlobalConstants.CAPTCHA_CODE_KEY + email);
        if (StringUtils.isBlank(code)) {
            recordLogininfor(tenantId, email, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        return code.equals(emailCode);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            recordLogininfor(TenantConstants.DEFAULT_TENANT_ID, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLogininfor(TenantConstants.DEFAULT_TENANT_ID, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"));
            throw new CaptchaException();
        }
    }

    private SysUserVo loadUserByUsername(String username) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
            .select(SysUser::getUserName, SysUser::getStatus)
            .eq(SysUser::getUserName, username));
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UserException("user.not.exists", username);
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new UserException("user.blocked", username);
        }
        return userMapper.selectUserByUserName(username);
    }

    private SysUserVo loadUserByPhonenumber(String phonenumber) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
            .select(SysUser::getPhonenumber, SysUser::getStatus)
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

    private SysUserVo loadUserByEmail(String email) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
            .select(SysUser::getPhonenumber, SysUser::getStatus)
            .eq(SysUser::getEmail, email));
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", email);
            throw new UserException("user.not.exists", email);
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", email);
            throw new UserException("user.blocked", email);
        }
        return userMapper.selectUserByEmail(email);
    }

    private SysUserVo loadUserByOpenid(String openid) {
        // 使用 openid 查询绑定用户 如未绑定用户 则根据业务自行处理 例如 创建默认用户
        // todo 自行实现 userService.selectUserByOpenid(openid);
        SysUserVo user = new SysUserVo();
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", openid);
            // todo 用户不存在 业务逻辑自行实现
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", openid);
            // todo 用户已被停用 业务逻辑自行实现
        }
        return user;
    }

    /**
     * 构建登录用户
     */
    private LoginUser buildLoginUser(SysUserVo user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUserId(user.getUserId());
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUsername(user.getUserName());
        loginUser.setUserType(user.getUserType());
        loginUser.setMenuPermission(permissionService.getMenuPermission(user.getUserId()));
        loginUser.setRolePermission(permissionService.getRolePermission(user.getUserId()));
        loginUser.setDeptName(ObjectUtil.isNull(user.getDept()) ? "" : user.getDept().getDeptName());
        List<RoleDTO> roles = BeanUtil.copyToList(user.getRoles(), RoleDTO.class);
        loginUser.setRoles(roles);
        return loginUser;
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(ServletUtils.getClientIP());
        sysUser.setLoginDate(DateUtils.getNowDate());
        sysUser.setUpdateBy(userId);
        userMapper.updateById(sysUser);
    }

    /**
     * 登录校验
     */
    private void checkLogin(LoginType loginType, String tenantId, String username, Supplier<Boolean> supplier) {
        String errorKey = GlobalConstants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;
        Integer maxRetryCount = userLoginConfig.getMaxRetryCount();
        Duration lockTime = userLoginConfig.getLockTime();

        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        Integer errorNumber = RedisUtils.getObject(errorKey);
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime.getSeconds());
        }

        if (supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                RedisUtils.setObject(errorKey, errorNumber, lockTime);
                recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime.getSeconds());
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setObject(errorKey, errorNumber);
                recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitCount(), errorNumber));
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }

        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
    }

    private void checkTenant(String tenantId) {
        if (!TenantHelper.isEnable()) {
            return;
        }
        if (TenantConstants.DEFAULT_TENANT_ID.equals(tenantId)) {
            return;
        }
        SysTenantVo tenant = tenantService.queryByTenantId(tenantId);
        if (ObjectUtil.isNull(tenant)) {
            log.info("登录租户：{} 不存在.", tenantId);
            throw new TenantException("tenant.not.exists");
        } else if (TenantStatus.DISABLE.getCode().equals(tenant.getStatus())) {
            log.info("登录租户：{} 已被停用.", tenantId);
            throw new TenantException("tenant.blocked");
        } else if (ObjectUtil.isNotNull(tenant.getExpireTime())
            && new Date().after(tenant.getExpireTime())) {
            log.info("登录租户：{} 已超过有效期.", tenantId);
            throw new TenantException("tenant.expired");
        }
    }

}
