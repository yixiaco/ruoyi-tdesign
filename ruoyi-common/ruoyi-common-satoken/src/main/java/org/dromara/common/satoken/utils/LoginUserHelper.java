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
import org.dromara.common.core.domain.model.BaseUser;
import org.dromara.common.core.enums.DeviceType;
import org.dromara.common.satoken.context.SaSecurityContext;

import java.util.List;
import java.util.Objects;
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
 * @author Lion Li、hexm
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginUserHelper {

    /**
     * 获取登录类型
     *
     * @return
     */
    public static String getLoginType() {
        return MultipleStpUtil.USER.getLoginType();
    }

    /**
     * 当前是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return MultipleStpUtil.USER.isLogin();
    }

    /**
     * 登录系统
     *
     * @param tokenUser 登录用户信息
     */
    public static void login(BaseUser tokenUser) {
        loginByDevice(tokenUser, null);
    }

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param baseUser 登录用户信息
     */
    public static void loginByDevice(BaseUser baseUser, DeviceType deviceType) {
        baseUser.setLoginType(getLoginType());
        SaStorage storage = SaHolder.getStorage();
        storage.set(getLoginType() + LOGIN_USER_KEY, baseUser);
        storage.set(getLoginType() + TENANT_KEY, baseUser.getTenantId());
        storage.set(getLoginType() + USER_KEY, baseUser.getUserId());
        SaLoginModel model = new SaLoginModel();
        if (ObjectUtil.isNotNull(deviceType)) {
            model.setDevice(deviceType.getDevice());
        }
        SaSecurityContext.setContext(baseUser);
        MultipleStpUtil.USER.login(baseUser.getUserId(), model);
        MultipleStpUtil.USER.getTokenSession().set(LOGIN_USER_KEY, baseUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseUser> T getUser() {
        return StorageUtil.getStorageIfAbsentSet(getLoginType() + LOGIN_USER_KEY, () -> {
            SaSession session = MultipleStpUtil.USER.getTokenSession();
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
    public static <T extends BaseUser> T getUser(String token) {
        try {
            SaSession session = MultipleStpUtil.USER.getTokenSessionByToken(token);
            return (T) session.get(LOGIN_USER_KEY);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 更新用户
     *
     * @param updateBy 更新回调
     */
    public static <T extends BaseUser> void updateUser(Consumer<T> updateBy) {
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
    public static <T extends BaseUser> void updateUser(Long userId, Consumer<T> updateBy) {
        List<String> tokens = MultipleStpUtil.USER.getTokenValueListByLoginId(userId);
        String tokenValue = null;
        try {
            tokenValue = MultipleStpUtil.USER.getTokenValue();
        } catch (Exception ignore) {
            // 不做处理
        }
        if (CollUtil.isNotEmpty(tokens)) {
            for (String token : tokens) {
                SaSession session = MultipleStpUtil.USER.getTokenSessionByToken(token);
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
        return StorageUtil.getStorageIfAbsentSet(getLoginType() + USER_KEY, () -> {
            BaseUser user = getUser();
            if (user != null) {
                return user.getUserId();
            }
            return null;
        }, Convert::toLong);
    }

    /**
     * 获取租户ID
     */
    public static String getTenantId() {
        return StorageUtil.getStorageIfAbsentSet(getLoginType() + TENANT_KEY, () -> {
            BaseUser user = getUser();
            if (user != null) {
                return user.getTenantId();
            }
            return null;
        });
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        BaseUser user = getUser();
        return Objects.requireNonNull(user).getUsername();
    }

}
