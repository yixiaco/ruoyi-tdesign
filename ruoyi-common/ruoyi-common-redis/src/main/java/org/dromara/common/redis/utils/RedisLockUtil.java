package org.dromara.common.redis.utils;

import org.redisson.api.RLock;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * redis锁封装工具
 *
 * @author hexm
 * @date 2023/03/04 17:49
 */
public class RedisLockUtil {

    /**
     * 获取并保存到缓存中
     *
     * @param key             缓存key
     * @param defaultSupplier 不存在值时回调
     */
    public static <T> T getOrSave(String key, Supplier<T> defaultSupplier) {
        return getOrAfter(key, null, null, defaultSupplier, RedisUtils::setCacheObject);
    }

    /**
     * 获取并保存到缓存中，设置获取锁后保留锁的最长时间
     *
     * @param key             缓存key
     * @param leaseTime       如果尚未通过调用unlock释放锁，则在获取锁后保留锁的最长时间。如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit            时间单位
     * @param defaultSupplier 不存在值时回调
     */
    public static <T> T getOrSave(String key, Long leaseTime, TimeUnit unit, Supplier<T> defaultSupplier) {
        return getOrAfter(key, leaseTime, unit, defaultSupplier, RedisUtils::setCacheObject);
    }

    /**
     * 获取并保存到缓存中
     *
     * @param key             缓存key
     * @param defaultSupplier 不存在值时回调
     */
    public static <T> List<T> getOrSaveList(String key, Supplier<List<T>> defaultSupplier) {
        return getOrAfter(key, null, null, defaultSupplier, RedisUtils::setCacheList);
    }

    /**
     * 获取并保存到缓存中，设置指定获取锁后保留锁的最长时间
     *
     * @param key             缓存key
     * @param leaseTime       如果尚未通过调用unlock释放锁，则在获取锁后保留锁的最长时间。如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit            时间单位
     * @param defaultSupplier 不存在值时回调
     */
    public static <T> List<T> getOrSaveList(String key, Long leaseTime, TimeUnit unit, Supplier<List<T>> defaultSupplier) {
        return getOrAfter(key, leaseTime, unit, defaultSupplier, RedisUtils::setCacheList);
    }


    /**
     * 获取并保存到缓存中
     *
     * @param key             缓存key
     * @param defaultSupplier 不存在值时回调
     */
    public static <T> Set<T> getOrSaveSet(String key, Supplier<Set<T>> defaultSupplier) {
        return getOrAfter(key, null, null, defaultSupplier, RedisUtils::setCacheSet);
    }

    /**
     * 获取并保存到缓存中，设置指定获取锁后保留锁的最长时间
     *
     * @param key             缓存key
     * @param leaseTime       如果尚未通过调用unlock释放锁，则在获取锁后保留锁的最长时间。如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit            时间单位
     * @param defaultSupplier 不存在值时回调
     */
    public static <T> Set<T> getOrSaveSet(String key, Long leaseTime, TimeUnit unit, Supplier<Set<T>> defaultSupplier) {
        return getOrAfter(key, leaseTime, unit, defaultSupplier, RedisUtils::setCacheSet);
    }

    /**
     * 获取并保存到缓存中
     *
     * @param key             缓存key
     * @param defaultSupplier 不存在值时回调
     */
    public static <T> Map<String, T> getOrSaveMap(String key, Supplier<Map<String, T>> defaultSupplier) {
        return getOrAfter(key, null, null, defaultSupplier, RedisUtils::setCacheMap);
    }

    /**
     * 获取并保存到缓存中，设置指定获取锁后保留锁的最长时间
     *
     * @param key             缓存key
     * @param leaseTime       如果尚未通过调用unlock释放锁，则在获取锁后保留锁的最长时间。如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit            时间单位
     * @param defaultSupplier 不存在值时回调
     */
    public static <T> Map<String, T> getOrSaveMap(String key, Long leaseTime, TimeUnit unit, Supplier<Map<String, T>> defaultSupplier) {
        return getOrAfter(key, leaseTime, unit, defaultSupplier, RedisUtils::setCacheMap);
    }

    /**
     * 获取缓存时，如果没有缓存时执行回调，并执行一个随后的操作
     *
     * @param key             缓存key
     * @param leaseTime       如果尚未通过调用unlock释放锁，则在获取锁后保留锁的最长时间。如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit            时间单位
     * @param defaultSupplier 不存在值时回调
     * @param after           没有缓存时执行的回调
     */
    public static <T> T getOrAfter(String key, Long leaseTime, TimeUnit unit, Supplier<T> defaultSupplier, BiConsumer<String, T> after) {
        T data = RedisUtils.getCacheObject(key);
        if (data == null) {
            RLock lock = RedisUtils.getClient().getLock("lock:" + key);
            if (leaseTime == null || leaseTime <= 0 || unit == null) {
                lock.lock();
            } else {
                lock.lock(leaseTime, unit);
            }
            try {
                data = RedisUtils.getCacheObject(key);
                if (data == null) {
                    data = defaultSupplier.get();
                    after.accept(key, data);
                }
            } finally {
                lock.unlock();
            }
        }
        return data;
    }
}
