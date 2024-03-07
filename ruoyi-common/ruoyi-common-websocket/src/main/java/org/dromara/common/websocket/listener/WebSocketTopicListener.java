package org.dromara.common.websocket.listener;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.websocket.holder.WebSocketSessionHolder;
import org.dromara.common.websocket.utils.WebSocketUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

/**
 * WebSocket 主题订阅监听器
 *
 * @author zendwang
 */
@Slf4j
public class WebSocketTopicListener implements ApplicationRunner, Ordered {

    @Override
    public void run(ApplicationArguments args) {
        WebSocketUtils.subscribeMessage((message) -> {
            String loginType = message.getLoginType();
            log.info("WebSocket主题订阅收到消息session loginType={} keys={} message={}", loginType, message.getSessionKeys(), message.getMessage());
            // 如果key不为空就按照key发消息 如果为空就群发
            if (CollUtil.isNotEmpty(message.getSessionKeys())) {
                message.getSessionKeys().forEach(key -> {
                    if (WebSocketSessionHolder.existSession(loginType, key)) {
                        WebSocketUtils.sendMessage(loginType, key, message.getMessage());
                    }
                });
            } else {
                WebSocketSessionHolder.getSessionsAll(loginType).forEach(key -> {
                    WebSocketUtils.sendMessage(loginType, key, message.getMessage());
                });
            }
        });
        log.info("初始化WebSocket主题订阅监听器成功");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
