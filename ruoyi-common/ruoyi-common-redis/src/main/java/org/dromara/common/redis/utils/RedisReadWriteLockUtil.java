package org.dromara.common.redis.utils;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * redis读写锁封装工具
 * 读写锁定义，只要没有写入者， read lock可以同时被多个读线程持有。 write lock是独占的。
 * 在非公平模式下工作。因此读写锁定的顺序是未指定的
 * <p>
 * 使用场景：防止缓存击穿
 *
 * @author hexm
 * @date 2023/03/04 17:21
 */
public class RedisReadWriteLockUtil {

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> T readWrite(String key, Supplier<T> defaultSupplier) {
        return readWrite(key, defaultSupplier, RedisUtils::setObject);
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param expire          过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> T readWrite(String key, Duration expire, Supplier<T> defaultSupplier) {
        return readWrite(key, defaultSupplier, (keyS, data) -> RedisUtils.setObject(keyS, data, expire));
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> List<T> readWriteList(String key, Supplier<List<T>> defaultSupplier) {
        return readWriteList(key, defaultSupplier, RedisUtils::setList);
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param expire          过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> List<T> readWriteList(String key, Duration expire, Supplier<List<T>> defaultSupplier) {
        return readWriteList(key, defaultSupplier, (keyS, data) -> RedisUtils.setList(keyS, data, expire));
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Set<T> readWriteSet(String key, Supplier<Set<T>> defaultSupplier) {
        return readWriteSet(key, defaultSupplier, RedisUtils::setSet);
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param expire          过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Set<T> readWriteSet(String key, Duration expire, Supplier<Set<T>> defaultSupplier) {
        return readWriteSet(key, defaultSupplier, (keyS, data) -> RedisUtils.setSet(keyS, data, expire));
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Map<String, T> readWriteMap(String key, Supplier<Map<String, T>> defaultSupplier) {
        return readWriteMap(key, defaultSupplier, RedisUtils::setMap);
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param expire          过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Map<String, T> readWriteMap(String key, Duration expire, Supplier<Map<String, T>> defaultSupplier) {
        return readWriteMap(key, defaultSupplier, (keyS, data) -> RedisUtils.setMap(keyS, data, expire));
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @param after           执行默认回调获取值后的操作，例如存入缓存中
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> T readWrite(String key, Supplier<T> defaultSupplier, BiConsumer<String, T> after) {
        RReadWriteLock readWriteLock = RedisUtils.getClient().getReadWriteLock(getKey(key));
        RLock lock = readWriteLock.readLock();
        lock.lock();
        try {
            T data = RedisUtils.getObject(key);
            if (data == null && defaultSupplier != null) {
                RLock writeLock = readWriteLock.writeLock();
                writeLock.lock();
                try {
                    data = RedisUtils.getObject(key);
                    // 双重检查
                    if (data == null) {
                        data = defaultSupplier.get();
                        after.accept(key, data);
                    }
                } finally {
                    writeLock.unlock();
                }
            }
            return data;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @param after           执行默认回调获取值后的操作，例如存入缓存中
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> List<T> readWriteList(String key, Supplier<List<T>> defaultSupplier, BiConsumer<String, List<T>> after) {
        RReadWriteLock readWriteLock = RedisUtils.getClient().getReadWriteLock(getKey(key));
        RLock lock = readWriteLock.readLock();
        lock.lock();
        try {
            Boolean hasKey = RedisUtils.hasKey(key);
            if (hasKey) {
                return RedisUtils.getList(key);
            }
            List<T> data = null;
            if (defaultSupplier != null) {
                RLock writeLock = readWriteLock.writeLock();
                writeLock.lock();
                try {
                    // 双重检查
                    hasKey = RedisUtils.hasKey(key);
                    if (hasKey) {
                        return RedisUtils.getList(key);
                    }
                    data = defaultSupplier.get();
                    after.accept(key, data);
                } finally {
                    writeLock.unlock();
                }
            }
            return data;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @param after           执行默认回调获取值后的操作，例如存入缓存中
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Set<T> readWriteSet(String key, Supplier<Set<T>> defaultSupplier, BiConsumer<String, Set<T>> after) {
        RReadWriteLock readWriteLock = RedisUtils.getClient().getReadWriteLock(getKey(key));
        RLock lock = readWriteLock.readLock();
        lock.lock();
        try {
            Boolean hasKey = RedisUtils.hasKey(key);
            if (hasKey) {
                return RedisUtils.getSet(key);
            }
            Set<T> data = null;
            if (defaultSupplier != null) {
                RLock writeLock = readWriteLock.writeLock();
                writeLock.lock();
                try {
                    // 双重检查
                    hasKey = RedisUtils.hasKey(key);
                    if (hasKey) {
                        return RedisUtils.getSet(key);
                    }
                    data = defaultSupplier.get();
                    after.accept(key, data);
                } finally {
                    writeLock.unlock();
                }
            }
            return data;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @param after           执行默认回调获取值后的操作，例如存入缓存中
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Map<String, T> readWriteMap(String key, Supplier<Map<String, T>> defaultSupplier, BiConsumer<String, Map<String, T>> after) {
        RReadWriteLock readWriteLock = RedisUtils.getClient().getReadWriteLock(getKey(key));
        RLock lock = readWriteLock.readLock();
        lock.lock();
        try {
            Boolean hasKey = RedisUtils.hasKey(key);
            if (hasKey) {
                return RedisUtils.getMap(key);
            }
            Map<String, T> data = null;
            if (defaultSupplier != null) {
                RLock writeLock = readWriteLock.writeLock();
                writeLock.lock();
                try {
                    // 双重检查
                    hasKey = RedisUtils.hasKey(key);
                    if (hasKey) {
                        return RedisUtils.getMap(key);
                    }
                    data = defaultSupplier.get();
                    after.accept(key, data);
                } finally {
                    writeLock.unlock();
                }
            }
            return data;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 执行读取一个Object操作
     *
     * @param key 缓存key
     * @param <T>
     * @return
     */
    public static <T> T read(String key) {
        return read(key, RedisUtils::getObject);
    }

    /**
     * 执行读取一个List操作
     *
     * @param key 缓存key
     * @param <T>
     * @return
     */
    public static <T> List<T> readList(String key) {
        return read(key, RedisUtils::getList);
    }

    /**
     * 执行读取一个Set操作
     *
     * @param key 缓存key
     * @param <T>
     * @return
     */
    public static <T> Set<T> readSet(String key) {
        return read(key, RedisUtils::getSet);
    }

    /**
     * 执行读取一个Map操作
     *
     * @param key 缓存key
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> readMap(String key) {
        return read(key, RedisUtils::getMap);
    }

    /**
     * 执行读操作
     *
     * @param key 缓存key
     * @return
     */
    public static <T> T read(String key, Function<String, T> after) {
        RReadWriteLock readWriteLock = RedisUtils.getClient().getReadWriteLock(getKey(key));
        RLock lock = readWriteLock.readLock();
        lock.lock();
        try {
            return after.apply(key);
        } finally {
            lock.unlock();
        }
    }

    private static String getKey(String key) {
        return "readWriteLock:" + key;
    }

    /**
     * 执行写操作
     *
     * @param key             缓存key
     * @param defaultSupplier 获取值回调
     * @return 回调值
     */
    public static <T> T write(String key, Supplier<T> defaultSupplier) {
        return write(key, defaultSupplier, RedisUtils::setObject);
    }

    /**
     * 执行写操作
     *
     * @param key             缓存key
     * @param defaultSupplier 获取值回调
     * @return 回调值
     */
    public static <T> T write(String key, Duration expire, Supplier<T> defaultSupplier) {
        return write(key, defaultSupplier, (keyS, data) -> RedisUtils.setObject(keyS, data, expire));
    }

    /**
     * 执行写操作
     *
     * @param key             缓存key
     * @param defaultSupplier 获取值回调
     * @return 回调值
     */
    public static <T> List<T> writeList(String key, Supplier<List<T>> defaultSupplier) {
        return write(key, defaultSupplier, RedisUtils::setList);
    }

    /**
     * 执行写操作
     *
     * @param key             缓存key
     * @param defaultSupplier 获取值回调
     * @return 回调值
     */
    public static <T> List<T> writeList(String key, Duration expire, Supplier<List<T>> defaultSupplier) {
        return write(key, defaultSupplier, (keyS, data) -> RedisUtils.setList(keyS, data, expire));
    }

    /**
     * 执行写操作
     *
     * @param key             缓存key
     * @param defaultSupplier 获取值回调
     * @return 回调值
     */
    public static <T> Set<T> writeSet(String key, Supplier<Set<T>> defaultSupplier) {
        return write(key, defaultSupplier, RedisUtils::setSet);
    }

    /**
     * 执行写操作
     *
     * @param key             缓存key
     * @param defaultSupplier 获取值回调
     * @return 回调值
     */
    public static <T> Set<T> writeSet(String key, Duration expire, Supplier<Set<T>> defaultSupplier) {
        return write(key, defaultSupplier, (keyS, data) -> RedisUtils.setSet(keyS, data, expire));
    }

    /**
     * 执行写操作
     *
     * @param key             缓存key
     * @param defaultSupplier 获取值回调
     * @return 回调值
     */
    public static <T> Map<String, T> writeMap(String key, Supplier<Map<String, T>> defaultSupplier) {
        return write(key, defaultSupplier, RedisUtils::setMap);
    }

    /**
     * 执行写操作
     *
     * @param key             缓存key
     * @param defaultSupplier 获取值回调
     * @return 回调值
     */
    public static <T> Map<String, T> writeMap(String key, Duration expire, Supplier<Map<String, T>> defaultSupplier) {
        return write(key, defaultSupplier, (keyS, data) -> RedisUtils.setMap(keyS, data, expire));
    }

    /**
     * 执行写操作
     *
     * @param key             缓存key
     * @param defaultSupplier 获取值回调
     * @param after           执行回调获取值后的操作，例如存入缓存中
     * @return
     */
    public static <T> T write(String key, Supplier<T> defaultSupplier, BiConsumer<String, T> after) {
        RReadWriteLock readWriteLock = RedisUtils.getClient().getReadWriteLock(getKey(key));
        RLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            T data = defaultSupplier.get();
            after.accept(key, data);
            return data;
        } finally {
            writeLock.unlock();
        }
    }
}
