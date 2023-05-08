package org.dromara.common.satoken.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.common.core.constant.TenantConstants;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.domain.model.LoginUser;
import org.dromara.common.core.enums.DeviceType;
import org.dromara.common.core.enums.UserType;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static org.dromara.common.satoken.utils.MultipleStpUtil.*;

/**
 * 登录鉴权助手
 * <p>
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * deivce 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 * <p>
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    /**
     * 获取登录类型
     *
     * @return
     */
    public static String getLoginType() {
        return MultipleStpUtil.SYSTEM.getLoginType();
    }

    /**
     * 当前是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return MultipleStpUtil.SYSTEM.isLogin();
    }

    /**
     * 登录系统
     *
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
        loginByDevice(loginUser, null);
    }

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     */
    public static void loginByDevice(LoginUser loginUser, DeviceType deviceType) {
        loginUser.setLoginType(getLoginType());
        SaStorage storage = SaHolder.getStorage();
        storage.set(getLoginType() + LOGIN_USER_KEY, loginUser);
        storage.set(getLoginType() + TENANT_KEY, loginUser.getTenantId());
        storage.set(getLoginType() + USER_KEY, loginUser.getUserId());
        SaLoginModel model = new SaLoginModel();
        if (ObjectUtil.isNotNull(deviceType)) {
            model.setDevice(deviceType.getDevice());
        }
        MultipleStpUtil.SYSTEM.login(loginUser.getLoginId(),
            model.setExtra(TENANT_KEY, loginUser.getTenantId())
                .setExtra(USER_KEY, loginUser.getUserId()));
        MultipleStpUtil.SYSTEM.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    @SuppressWarnings("unchecked")
    public static <T extends LoginUser> T getUser() {
        T loginUser = (T) SaHolder.getStorage().get(getLoginType() + LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        SaSession session = MultipleStpUtil.SYSTEM.getTokenSession();
        if (session != null) {
            loginUser = (T) session.get(LOGIN_USER_KEY);
            SaHolder.getStorage().set(getLoginType() + LOGIN_USER_KEY, loginUser);
        }
        return loginUser;
    }

    /**
     * 获取用户基于token
     */
    @SuppressWarnings("unchecked")
    public static <T extends LoginUser> T getUser(String token) {
        return (T) MultipleStpUtil.SYSTEM.getTokenSessionByToken(token).get(LOGIN_USER_KEY);
    }

    /**
     * 更新用户
     *
     * @param updateBy 更新回调
     */
    public static <T extends LoginUser> void updateUser(Consumer<T> updateBy) {
        Long userId = getUserId();
        if (userId != null) {
            updateUser(userId, updateBy);
        }
    }

    /**
     * 更新用户
     *
     * @param userId   用户id
     * @param updateBy 更新回调
     */
    @SuppressWarnings("unchecked")
    public static <T extends LoginUser> void updateUser(Long userId, Consumer<T> updateBy) {
        List<String> tokens = MultipleStpUtil.SYSTEM.getTokenValueListByLoginId(userId);
        String tokenValue = MultipleStpUtil.SYSTEM.getTokenValue();
        if (CollUtil.isNotEmpty(tokens)) {
            for (String token : tokens) {
                SaSession session = MultipleStpUtil.SYSTEM.getTokenSessionByToken(token);
                if (session != null) {
                    T tokenUser = (T) session.get(LOGIN_USER_KEY);
                    updateBy.accept(tokenUser);
                    session.set(LOGIN_USER_KEY, tokenUser);
                    if (Objects.equals(tokenValue, token)) {
                        SaStorage storage = SaHolder.getStorage();
                        if (storage != null) {
                            storage.set(getLoginType() + LOGIN_USER_KEY, tokenUser);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        Long userId;
        try {
            userId = Convert.toLong(SaHolder.getStorage().get(getLoginType() + USER_KEY));
            if (ObjectUtil.isNull(userId)) {
                userId = Convert.toLong(MultipleStpUtil.SYSTEM.getExtra(USER_KEY));
                SaHolder.getStorage().set(getLoginType() + USER_KEY, userId);
            }
        } catch (Exception e) {
            return null;
        }
        return userId;
    }

    /**
     * 获取租户ID
     */
    public static String getTenantId() {
        String tenantId;
        try {
            tenantId = (String) SaHolder.getStorage().get(getLoginType() + TENANT_KEY);
            if (ObjectUtil.isNull(tenantId)) {
                tenantId = (String) MultipleStpUtil.SYSTEM.getExtra(TENANT_KEY);
                SaHolder.getStorage().set(getLoginType() + TENANT_KEY, tenantId);
            }
        } catch (Exception e) {
            return null;
        }
        return tenantId;
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        return getUser().getDeptId();
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        return getUser().getUsername();
    }

    /**
     * 获取用户类型
     */
    public static UserType getUserType() {
        String loginId = MultipleStpUtil.SYSTEM.getLoginIdAsString();
        return UserType.getUserType(loginId);
    }

    /**
     * 是否为超级管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isSuperAdmin(Long userId) {
        return UserConstants.SUPER_ADMIN_ID.equals(userId);
    }

    public static boolean isSuperAdmin() {
        return isSuperAdmin(getUserId());
    }

    /**
     * 是否为超级管理员
     *
     * @param rolePermission 角色权限标识组
     * @return 结果
     */
    public static boolean isTenantAdmin(Set<String> rolePermission) {
        return rolePermission.contains(TenantConstants.TENANT_ADMIN_ROLE_KEY);
    }

    public static boolean isTenantAdmin() {
        return isTenantAdmin(getUser().getRolePermission());
    }

}
