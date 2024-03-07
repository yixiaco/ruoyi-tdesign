package org.dromara.common.websocket.config.properties;

import lombok.Data;
import org.dromara.common.websocket.handler.ICustomWebSocketHandler;

import java.util.Set;

/**
 * WebSocket 配置项
 *
 * @author zendwang
 */
@Data
public class WebSocketProperties {

    /**
     * 匹配路径, 不填则会被过滤掉
     */
    private Set<String> paths;

    /**
     * 设置访问源地址
     */
    private String allowedOrigins;

    /**
     * 自定义处理器。支持spring bean或空构造方法初始化对象
     */
    private Class<? extends ICustomWebSocketHandler> handler;
}
