package org.dromara.system.service.impl;

import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpLogic;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.dromara.common.core.domain.model.BaseUser;
import org.dromara.common.satoken.context.SaSecurityContext;
import org.dromara.common.satoken.stp.DynamicStpLogic;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.sensitive.core.SensitiveService;
import org.dromara.common.tenant.helper.TenantHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 脱敏服务
 * 默认管理员不过滤
 * 需自行根据业务重写实现
 *
 * @author Lion Li
 * @version 3.6.0
 */
@Service
public class SysSensitiveServiceImpl implements SensitiveService {

    /**
     * 是否脱敏
     */
    @Override
    public boolean isSensitive(String[] roleKey, String[] perms, SaMode mode) {
        BaseUser user = SaSecurityContext.getContext();
        if (user == null) {
            return true;
        }
        List<String> roles = Arrays.stream(roleKey).filter(StrUtil::isNotBlank).toList();
        List<String> permissions = Arrays.stream(perms).filter(StrUtil::isNotBlank).toList();
        boolean roleExist = CollUtil.isNotEmpty(roles);
        boolean permsExist = CollUtil.isNotEmpty(permissions);
        StpLogic logic = DynamicStpLogic.getDynamicStpLogic(user.getLoginType());
        if (roleExist && permsExist) {
            if (mode == SaMode.OR && logic.hasRoleOr(roleKey) && logic.hasPermissionOr(perms)) {
                return false;
            } else if (logic.hasRoleAnd(roleKey) && logic.hasPermissionAnd(perms)) {
                return false;
            }
        } else if (mode == SaMode.OR && roleExist && logic.hasRoleOr(roleKey)) {
            return false;
        } else if (roleExist && logic.hasRoleAnd(roleKey)) {
            return false;
        } else if (mode == SaMode.OR && permsExist && logic.hasPermissionOr(perms)) {
            return false;
        } else if (permsExist && logic.hasPermissionAnd(perms)) {
            return false;
        }

        if (Objects.equals(user.getLoginType(), LoginHelper.getLoginType())) {
            if (TenantHelper.isEnable()) {
                return !LoginHelper.isSuperAdmin() && !LoginHelper.isTenantAdmin();
            }
            return !LoginHelper.isSuperAdmin();
        }

        return true;
    }

}
