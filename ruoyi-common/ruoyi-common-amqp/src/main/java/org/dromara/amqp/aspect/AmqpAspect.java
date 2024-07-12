package org.dromara.amqp.aspect;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.dromara.amqp.annotation.AmqpMessageDistinct;
import org.dromara.amqp.constant.KeyConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.spring.SpringExpressionCreated;
import org.dromara.common.redis.utils.RedisLockUtil;
import org.dromara.common.redis.utils.RedisUtils;
import org.redisson.api.RLock;
import org.springframework.core.annotation.Order;

import java.time.Duration;

/**
 * Amqp切面。仅支持方法层面
 *
 * @author hexm
 * @date 2024/06/22
 */
@Aspect
@Order(-2)
public class AmqpAspect {

    /**
     * 处理消息幂等
     *
     * @param point    织入点
     * @param distinct amqp去重注解
     * @return
     * @throws Throwable
     */
    @Around("@annotation(distinct)")
    public Object handleDistinct(ProceedingJoinPoint point, AmqpMessageDistinct distinct) throws Throwable {
        if (StrUtil.isBlank(distinct.value())) {
            throw new ServiceException("Amqp消息幂等未配置key！");
        }
        String key;
        if (distinct.value().contains(SpringExpressionCreated.PARSER_CONTEXT.getExpressionPrefix())) {
            key = SpringExpressionCreated.createStandardTemplate(point).getValueString(distinct.value());
        } else {
            key = SpringExpressionCreated.createStandard(point).getValueString(distinct.value());
        }
        String cacheKey;
        if (distinct.prefix() != null && !distinct.prefix().isEmpty()) {
            cacheKey = KeyConstants.REDIS_KEY_PREFIX + distinct.prefix() + ":" + key;
        } else {
            cacheKey = KeyConstants.REDIS_KEY_PREFIX + key;
        }
        if (RedisUtils.hasKey(cacheKey)) {
            return null;
        }
        RLock lock = RedisLockUtil.getLock(cacheKey);
        lock.lock();
        // 双重检查
        if (RedisUtils.hasKey(cacheKey)) {
            return null;
        }
        try {
            Object result = point.proceed();
            setCacheObject(cacheKey, distinct.expire());
            return result;
        } catch (Throwable e) {
            if (distinct.ignoreThrowable()) {
                setCacheObject(cacheKey, distinct.expire());
            }
            throw e;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置缓存
     *
     * @param cacheKey 缓存key
     * @param expire   缓存时间
     */
    private static void setCacheObject(String cacheKey, long expire) {
        if (expire > 0) {
            RedisUtils.setObject(cacheKey, true, Duration.ofSeconds(expire));
        } else {
            RedisUtils.setObject(cacheKey, true);
        }
    }
}
