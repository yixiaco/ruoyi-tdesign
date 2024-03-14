package org.dromara.common.satoken.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.hutool.core.util.ObjectUtil;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 存储操作工具类
 *
 * @author hexm
 * @date 2024/01/09 16:22
 */
public class StorageUtil {

    /**
     * 如果存储中不存在值，则执行handle操作
     *
     * @param key    key
     * @param handle 返回值操作
     */
    @SuppressWarnings("unchecked cast")
    public static <T> T getStorageIfAbsentSet(String key, Supplier<T> handle) {
        try {
            T obj = (T) SaHolder.getStorage().get(key);
            if (ObjectUtil.isNull(obj)) {
                obj = handle.get();
                if (ObjectUtil.isNotNull(obj)) {
                    SaHolder.getStorage().set(key, obj);
                }
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 如果存储中不存在值，则执行handle操作
     *
     * @param key     key
     * @param handle  返回值操作
     * @param convert 值转换器
     */
    public static <T> T getStorageIfAbsentSet(String key, Supplier<T> handle, Function<Object, T> convert) {
        try {
            T obj = convert.apply(SaHolder.getStorage().get(key));
            if (ObjectUtil.isNull(obj)) {
                obj = handle.get();
                if (ObjectUtil.isNotNull(obj)) {
                    SaHolder.getStorage().set(key, obj);
                }
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }
}
