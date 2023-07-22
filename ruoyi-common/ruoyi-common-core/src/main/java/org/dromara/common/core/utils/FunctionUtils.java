package org.dromara.common.core.utils;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 函数工具
 *
 * @author hexm
 * @date 2023/07/22 14:12
 */
public class FunctionUtils {

    /**
     * 请求的对象如果不为空，则执行回调，否则返回默认值回调
     *
     * @param obj          对象
     * @param function     不为空的回调
     * @param defaultValue 默认值回调
     */
    public static <T, R> R getNonNullElseGet(T obj, Function<T, R> function, Supplier<R> defaultValue) {
        if (obj == null) {
            return defaultValue.get();
        }
        return function.apply(obj);
    }

    /**
     * 请求的对象如果不为空，则执行回调，否则返回默认值回调
     *
     * @param obj          对象
     * @param function     不为空的回调
     * @param defaultValue 默认值
     */
    public static <T, R> R getNonNullElseGet(T obj, Function<T, R> function, R defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        return function.apply(obj);
    }

    /**
     * 如果请求不为空，则执行回调
     *
     * @param obj      对象
     * @param function 回调
     * @return
     */
    public static <T, R> R getNonNull(T obj, Function<T, R> function) {
        if (obj != null) {
            return function.apply(obj);
        }
        return null;
    }
}
