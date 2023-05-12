package org.dromara.common.satoken.stp;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.error.SaErrorCode;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.stp.StpLogic;

import java.util.List;

/**
 * Sa-Token 多配置权限认证，逻辑实现类
 *
 * @author hexm
 * @date 2023/04/24 14:21
 */
public class MultipleStpLogic extends StpLogic implements MultipleStpLogicInterface {

    private final SaTokenConfig config;

    /**
     * 初始化StpLogic, 并指定账号类型
     *
     * @param loginType 账号体系标识
     * @param config    配置
     */
    public MultipleStpLogic(String loginType, SaTokenConfig config) {
        super(loginType);
        this.config = config;
    }

    @Override
    public SaTokenConfig getConfig() {
        return config;
    }

    @Override
    public void checkPermissionAnd(String... permissionArray) {
        Object loginId = getLoginId();
        if (permissionArray.length == 0) {
            return;
        }
        List<String> permissionList = getPermissionList(loginId);
        for (String permission : permissionArray) {
            if (!hasElement(permissionList, permission)) {
                throw new NotPermissionException(permission, this.loginType).setCode(SaErrorCode.CODE_11051);
            }
        }
    }

    @Override
    public void checkPermissionOr(String... permissionArray) {
        Object loginId = getLoginId();
        if (permissionArray.length == 0) {
            return;
        }
        List<String> permissionList = getPermissionList(loginId);
        for (String permission : permissionArray) {
            if (hasElement(permissionList, permission)) {
                // 有的话提前退出
                return;
            }
        }
        throw new NotPermissionException(permissionArray[0], this.loginType).setCode(SaErrorCode.CODE_11051);
    }

    /**
     * 校验：当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
     *
     * @param roleArray 角色标识数组
     */
    @Override
    public void checkRoleAnd(String... roleArray) {
        Object loginId = getLoginId();
        if (roleArray.length == 0) {
            return;
        }
        List<String> roleList = getRoleList(loginId);
        for (String role : roleArray) {
            if (!hasElement(roleList, role)) {
                throw new NotRoleException(role, this.loginType).setCode(SaErrorCode.CODE_11041);
            }
        }
    }

    @Override
    public void checkRoleOr(String... roleArray) {
        Object loginId = getLoginId();
        if (roleArray.length == 0) {
            return;
        }
        List<String> roleList = getRoleList(loginId);
        for (String role : roleArray) {
            if (hasElement(roleList, role)) {
                // 有的话提前退出
                return;
            }
        }
        throw new NotRoleException(roleArray[0], this.loginType).setCode(SaErrorCode.CODE_11041);
    }
}
