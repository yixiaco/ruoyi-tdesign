package org.dromara.system.listener;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.system.service.ISysSensitiveWordService;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 敏感词监听器
 *
 * @author hexm
 * @date 2024/8/19
 */
@Component
@RequiredArgsConstructor
public class SensitiveWordRedisMQListener implements InitializingBean, DisposableBean, MessageListener<String> {

    public static final String CHANNEL_KEY = GlobalConstants.GLOBAL_REDIS_KEY + "sensitiveWord_channelKey";
    private final ISysSensitiveWordService sensitiveWordService;

    @Override
    public void afterPropertiesSet() throws Exception {
        RedisUtils.subscribe(CHANNEL_KEY, String.class, this);
    }

    @Override
    public void destroy() throws Exception {
        RedisUtils.unsubscribe(CHANNEL_KEY, this);
    }

    @Override
    public void onMessage(CharSequence channel, String msg) {
        sensitiveWordService.initSensitiveWordReplacers();
    }
}
