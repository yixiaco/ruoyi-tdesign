package org.dromara.common.websocket.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * WebSocket 配置项
 *
 * @author zendwang
 */
@ConfigurationProperties("websocket")
@Data
public class MultipleWebSocketProperties {

    /** 是否启用websocket */
    private Boolean enabled;

    /** 多账号体系配置,登录类型对应的配置 */
    private Map<String, WebSocketProperties> multiple;

    /**
     * 获取匹配的第一个登录类型
     *
     * @param path 路径
     */
    public String getMatchLoginType(String path) {
        if (multiple != null) {
            for (Map.Entry<String, WebSocketProperties> entry : multiple.entrySet()) {
                WebSocketProperties properties = entry.getValue();
                if (properties != null && properties.getPaths() != null && properties.getPaths().contains(path)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}
