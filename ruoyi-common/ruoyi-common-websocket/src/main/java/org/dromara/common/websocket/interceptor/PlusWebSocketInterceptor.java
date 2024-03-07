package org.dromara.common.websocket.interceptor;

import cn.dev33.satoken.stp.StpLogic;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.model.BaseUser;
import org.dromara.common.satoken.stp.DynamicStpLogic;
import org.dromara.common.satoken.utils.MultipleLoginBaseHelper;
import org.dromara.common.websocket.config.properties.MultipleWebSocketProperties;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import static org.dromara.common.websocket.constant.WebSocketConstants.LOGIN_USER_KEY;

/**
 * WebSocket握手请求的拦截器
 *
 * @author zendwang
 */
@Slf4j
public class PlusWebSocketInterceptor implements HandshakeInterceptor {

    private final MultipleWebSocketProperties multipleWebSocketProperties;

    public PlusWebSocketInterceptor(MultipleWebSocketProperties multipleWebSocketProperties) {
        this.multipleWebSocketProperties = multipleWebSocketProperties;
    }

    /**
     * 握手前
     *
     * @param request    request
     * @param response   response
     * @param wsHandler  wsHandler
     * @param attributes attributes
     * @return 是否握手成功
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String loginType = multipleWebSocketProperties.getMatchLoginType(request.getURI().getPath());
        if (loginType != null) {
            StpLogic logic = DynamicStpLogic.getDynamicStpLogic(loginType);
            BaseUser user = MultipleLoginBaseHelper.getUser(logic);
            attributes.put(LOGIN_USER_KEY, user);
            return true;
        }
        return false;
    }

    /**
     * 握手后
     *
     * @param request   request
     * @param response  response
     * @param wsHandler wsHandler
     * @param exception 异常
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
