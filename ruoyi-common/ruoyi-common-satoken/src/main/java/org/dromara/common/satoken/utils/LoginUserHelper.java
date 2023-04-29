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
import org.dromara.common.core.domain.model.TokenUser;
import org.dromara.common.core.enums.DeviceType;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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

    public static final String LOGIN_USER_KEY = "user:loginUser";
    public static final String TENANT_KEY = "user:tenantId";
    public static final String USER_KEY = "user:userId";

    /**
     * 登录系统
     *
     * @param tokenUser 登录用户信息
     */
    public static void login(TokenUser tokenUser) {
        loginByDevice(tokenUser, null);
    }

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param tokenUser 登录用户信息
     */
    public static void loginByDevice(TokenUser tokenUser, DeviceType deviceType) {
        SaStorage storage = SaHolder.getStorage();
        storage.set(LOGIN_USER_KEY, tokenUser);
        storage.set(TENANT_KEY, tokenUser.getTenantId());
        storage.set(USER_KEY, tokenUser.getUserId());
        SaLoginModel model = new SaLoginModel();
        if (ObjectUtil.isNotNull(deviceType)) {
            model.setDevice(deviceType.getDevice());
        }
        MultipleStpUtil.USER.login(tokenUser.getUserId(),
            model.setExtra(TENANT_KEY, tokenUser.getTenantId())
                .setExtra(USER_KEY, tokenUser.getUserId()));
        MultipleStpUtil.USER.getTokenSession().set(LOGIN_USER_KEY, tokenUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    @SuppressWarnings("unchecked")
    public static <T extends TokenUser> T getLoginUser() {
        T tokenUser = (T) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (tokenUser != null) {
            return tokenUser;
        }
        SaSession session = MultipleStpUtil.USER.getTokenSession();
        if (session != null) {
            tokenUser = (T) session.get(LOGIN_USER_KEY);
            SaHolder.getStorage().set(LOGIN_USER_KEY, tokenUser);
        }
        return tokenUser;
    }

    /**
     * 获取用户基于token
     */
    @SuppressWarnings("unchecked")
    public static <T extends TokenUser> T getLoginUser(String token) {
        return (T) MultipleStpUtil.USER.getTokenSessionByToken(token).get(LOGIN_USER_KEY);
    }

    /**
     * 更新用户
     *
     * @param updateBy 更新回调
     */
    public static <T extends TokenUser> void updateUser(Consumer<T> updateBy) {
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
    public static <T extends TokenUser> void updateUser(Long userId, Consumer<T> updateBy) {
        List<String> tokens = MultipleStpUtil.USER.getTokenValueListByLoginId(userId);
        String tokenValue = MultipleStpUtil.USER.getTokenValue();
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
                            storage.set(LOGIN_USER_KEY, tokenUser);
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
            userId = Convert.toLong(SaHolder.getStorage().get(USER_KEY));
            if (ObjectUtil.isNull(userId)) {
                userId = Convert.toLong(MultipleStpUtil.USER.getExtra(USER_KEY));
                SaHolder.getStorage().set(USER_KEY, userId);
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
            tenantId = (String) SaHolder.getStorage().get(TENANT_KEY);
            if (ObjectUtil.isNull(tenantId)) {
                tenantId = (String) MultipleStpUtil.USER.getExtra(TENANT_KEY);
                SaHolder.getStorage().set(TENANT_KEY, tenantId);
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
        return getLoginUser().getUsername();
    }

}
