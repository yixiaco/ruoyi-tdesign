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
        MultipleStpUtil.USER.login(baseUser.getUserId(),
            model.setExtra(TENANT_KEY, baseUser.getTenantId())
                .setExtra(USER_KEY, baseUser.getUserId()));
        MultipleStpUtil.USER.getTokenSession().set(LOGIN_USER_KEY, baseUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseUser> T getUser() {
        T user = (T) SaHolder.getStorage().get(getLoginType() + LOGIN_USER_KEY);
        if (user != null) {
            return user;
        }
        SaSession session = MultipleStpUtil.USER.getTokenSession();
        if (session != null) {
            user = (T) session.get(LOGIN_USER_KEY);
            SaHolder.getStorage().set(getLoginType() + LOGIN_USER_KEY, user);
        }
        return user;
    }

    /**
     * 获取用户基于token
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseUser> T getUser(String token) {
        return (T) MultipleStpUtil.USER.getTokenSessionByToken(token).get(LOGIN_USER_KEY);
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
        Long userId;
        try {
            userId = Convert.toLong(SaHolder.getStorage().get(getLoginType() + USER_KEY));
            if (ObjectUtil.isNull(userId)) {
                userId = Convert.toLong(MultipleStpUtil.USER.getExtra(USER_KEY));
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
                tenantId = (String) MultipleStpUtil.USER.getExtra(TENANT_KEY);
                SaHolder.getStorage().set(getLoginType() + TENANT_KEY, tenantId);
            }
        } catch (Exception e) {
            return null;
        }
        return tenantId;
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        return getUser().getUsername();
    }

}
