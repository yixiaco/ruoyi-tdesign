package org.dromara.common.satoken.context;

import cn.dev33.satoken.context.SaHolder;
import org.dromara.common.core.domain.model.BaseUser;

import java.util.Optional;

/**
 * 授权上下文对象
 *
 * @author hexm
 * @date 2023/05/08 15:19
 */
public class SaSecurityContext {

    private static final String KEY = "SaToken-SecurityContext";

    /**
     * 设置上下文对象
     *
     * @param context 上下文对象
     */
    public static <T extends BaseUser> void setContext(T context) {
        SaHolder.getStorage().set(KEY, context);
    }

    /**
     * 获取当前下文对象
     *
     * @return
     */
    @SuppressWarnings("unchecked cast")
    public static <T extends BaseUser> T getContext() {
        try {
            return (T) SaHolder.getStorage().get(KEY);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前下文对象
     *
     * @return
     */
    public static <T extends BaseUser> Optional<T> getContextOptional() {
        return Optional.ofNullable(getContext());
    }
}
