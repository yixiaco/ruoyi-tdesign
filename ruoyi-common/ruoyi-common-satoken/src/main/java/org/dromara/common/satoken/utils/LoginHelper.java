package org.dromara.common.satoken.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.common.core.constant.TenantConstants;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.domain.model.LoginUser;
import org.dromara.common.core.enums.UserType;
import org.dromara.common.satoken.context.SaSecurityContext;

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

    public static final String CLIENT_KEY = "clientId";

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
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     * @param model     配置参数
     */
    public static void login(LoginUser loginUser, SaLoginModel model) {
        loginUser.setLoginType(getLoginType());
        SaStorage storage = SaHolder.getStorage();
        storage.set(getLoginType() + LOGIN_USER_KEY, loginUser);
        storage.set(getLoginType() + TENANT_KEY, loginUser.getTenantId());
        storage.set(getLoginType() + USER_KEY, loginUser.getUserId());
        model = ObjectUtil.defaultIfNull(model, new SaLoginModel());
        SaSecurityContext.setContext(loginUser);
        MultipleStpUtil.SYSTEM.login(loginUser.getLoginId(), model);
        MultipleStpUtil.SYSTEM.getSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    @SuppressWarnings("unchecked")
    public static <T extends LoginUser> T getUser() {
        return StorageUtil.getStorageIfAbsentSet(getLoginType() + LOGIN_USER_KEY, () -> {
            SaSession session = MultipleStpUtil.SYSTEM.getSession();
            if (session != null) {
                return (T) session.get(LOGIN_USER_KEY);
            }
            return null;
        });
    }

    /**
     * 获取用户基于token
     */
    @SuppressWarnings("unchecked")
    public static <T extends LoginUser> T getUser(String token) {
        Object loginId = MultipleStpUtil.SYSTEM.getLoginIdByToken(token);
        SaSession session = MultipleStpUtil.SYSTEM.getSessionByLoginId(loginId);
        if (session == null) {
            return null;
        }
        return (T) session.get(LOGIN_USER_KEY);
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
        SaSession session = MultipleStpUtil.SYSTEM.getSessionByLoginId(userId);
        if (session != null) {
            T tokenUser = (T) session.get(LOGIN_USER_KEY);
            updateBy.accept(tokenUser);
            session.set(LOGIN_USER_KEY, tokenUser);
            SaStorage storage = SaHolder.getStorage();
            if (storage != null) {
                storage.set(getLoginType() + LOGIN_USER_KEY, tokenUser);
            }
        }
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        return StorageUtil.getStorageIfAbsentSet(getLoginType() + USER_KEY, () -> getUser().getUserId(), Convert::toLong);
    }

    /**
     * 获取租户ID
     */
    public static String getTenantId() {
        return StorageUtil.getStorageIfAbsentSet(getLoginType() + TENANT_KEY, () -> getUser().getTenantId());
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
        String loginType = MultipleStpUtil.SYSTEM.getLoginIdAsString();
        return UserType.getUserType(loginType);
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
