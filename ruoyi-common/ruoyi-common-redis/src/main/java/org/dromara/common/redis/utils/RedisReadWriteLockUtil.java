package org.dromara.common.redis.utils;

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
     * @param expire          过期时间
     * @param rewriteExpire   是否重写过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> T readWrite(String key, Duration expire, boolean rewriteExpire, Supplier<T> defaultSupplier) {
        return readWrite(key, expire, rewriteExpire, defaultSupplier, (keyS, data) -> RedisUtils.setObject(keyS, data, expire));
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param readWriteLock   构造数据
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> T readWrite(RedisReadWriteLockBuild readWriteLock, Supplier<T> defaultSupplier) {
        return readWriteLock.readWrite(defaultSupplier, (keyS, data) -> {
            Duration expire = readWriteLock.getExpire();
            if (expire != null) {
                RedisUtils.setObject(keyS, data, expire);
            } else {
                RedisUtils.setObject(keyS, data);
            }
        });
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
     * @param rewriteExpire   是否重写过期时间
     * @param expire          过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> List<T> readWriteList(String key, Duration expire, boolean rewriteExpire, Supplier<List<T>> defaultSupplier) {
        return readWriteList(key, expire, rewriteExpire, defaultSupplier, (keyS, data) -> RedisUtils.setList(keyS, data, expire));
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param readWriteLock   构造数据
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> List<T> readWriteList(RedisReadWriteLockBuild readWriteLock, Supplier<List<T>> defaultSupplier) {
        return readWriteLock.readWriteList(defaultSupplier, (keyS, data) -> {
            Duration expire = readWriteLock.getExpire();
            if (expire != null) {
                RedisUtils.setList(keyS, data, expire);
            } else {
                RedisUtils.setList(keyS, data);
            }
        });
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
     * @param rewriteExpire   是否重写过期时间
     * @param expire          过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Set<T> readWriteSet(String key, Duration expire, boolean rewriteExpire, Supplier<Set<T>> defaultSupplier) {
        return readWriteSet(key, expire, rewriteExpire, defaultSupplier, (keyS, data) -> RedisUtils.setSet(keyS, data, expire));
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param readWriteLock   构造数据
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Set<T> readWriteSet(RedisReadWriteLockBuild readWriteLock, Supplier<Set<T>> defaultSupplier) {
        return readWriteLock.readWriteSet(defaultSupplier, (keyS, data) -> {
            Duration expire = readWriteLock.getExpire();
            if (expire != null) {
                RedisUtils.setSet(keyS, data, expire);
            } else {
                RedisUtils.setSet(keyS, data);
            }
        });
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
     * @param expire          过期时间
     * @param rewriteExpire   是否重写过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Map<String, T> readWriteMap(String key, Duration expire, boolean rewriteExpire, Supplier<Map<String, T>> defaultSupplier) {
        return readWriteMap(key, expire, rewriteExpire, defaultSupplier, (keyS, data) -> RedisUtils.setMap(keyS, data, expire));
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param readWriteLock   构造数据
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Map<String, T> readWriteMap(RedisReadWriteLockBuild readWriteLock, Supplier<Map<String, T>> defaultSupplier) {
        return readWriteLock.readWriteMap(defaultSupplier, (keyS, data) -> {
            Duration expire = readWriteLock.getExpire();
            if (expire != null) {
                RedisUtils.setMap(keyS, data, expire);
            } else {
                RedisUtils.setMap(keyS, data);
            }
        });
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
        return readWrite(key, null, false, defaultSupplier, after);
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param expire          过期时间
     * @param rewriteExpire   是否重写过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @param after           执行默认回调获取值后的操作，例如存入缓存中
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> T readWrite(String key, Duration expire, boolean rewriteExpire, Supplier<T> defaultSupplier, BiConsumer<String, T> after) {
        return RedisReadWriteLockBuild.builder()
            .key(key)
            .expire(expire)
            .rewriteExpire(rewriteExpire)
            .build()
            .readWrite(defaultSupplier, after);
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
        return readWriteList(key, null, false, defaultSupplier, after);
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param expire          过期时间
     * @param rewriteExpire   是否重写过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @param after           执行默认回调获取值后的操作，例如存入缓存中
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> List<T> readWriteList(String key, Duration expire, boolean rewriteExpire, Supplier<List<T>> defaultSupplier, BiConsumer<String, List<T>> after) {
        return RedisReadWriteLockBuild.builder()
            .key(key)
            .expire(expire)
            .rewriteExpire(rewriteExpire)
            .build()
            .readWriteList(defaultSupplier, after);
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
        return readWriteSet(key, null, false, defaultSupplier, after);
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param expire          过期时间
     * @param rewriteExpire   是否重写过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @param after           执行默认回调获取值后的操作，例如存入缓存中
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Set<T> readWriteSet(String key, Duration expire, boolean rewriteExpire, Supplier<Set<T>> defaultSupplier, BiConsumer<String, Set<T>> after) {
        return RedisReadWriteLockBuild.builder()
            .key(key)
            .expire(expire)
            .rewriteExpire(rewriteExpire)
            .build()
            .readWriteSet(defaultSupplier, after);
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
        return readWriteMap(key, null, false, defaultSupplier, after);
    }

    /**
     * 同时执行读与写操作，当读取值不存在时，执行默认回调获取值
     *
     * @param key             key
     * @param expire          过期时间
     * @param rewriteExpire   是否重写过期时间
     * @param defaultSupplier 默认值回调
     * @param <T>             数据类型
     * @param after           执行默认回调获取值后的操作，例如存入缓存中
     * @return 返回缓存的数据，如果不存在则在执行回调后返回回调值
     */
    public static <T> Map<String, T> readWriteMap(String key, Duration expire, boolean rewriteExpire, Supplier<Map<String, T>> defaultSupplier, BiConsumer<String, Map<String, T>> after) {
        return RedisReadWriteLockBuild.builder()
            .key(key)
            .expire(expire)
            .rewriteExpire(rewriteExpire)
            .build()
            .readWriteMap(defaultSupplier, after);
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
        return RedisReadWriteLockBuild.builder()
            .key(key)
            .build()
            .read(after);
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
     * 执行写操作,并保留过期时间
     *
     * @param key             缓存key
     * @param defaultSupplier 获取值回调
     * @return 回调值
     */
    public static <T> T writeAndKeepTTL(String key, Supplier<T> defaultSupplier) {
        return write(key, defaultSupplier, (s, t) -> RedisUtils.setObject(s, t, true));
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
     * 执行删除操作
     *
     * @param key 缓存key
     */
    public static void remove(String key) {
        RedisReadWriteLockBuild.builder().key(key).build().remove();
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
        return RedisReadWriteLockBuild.builder()
            .key(key)
            .build()
            .write(defaultSupplier, after);
    }
}
