package org.dromara.common.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.model.BaseUser;
import org.dromara.common.websocket.dto.WebSocketMessageDto;
import org.dromara.common.websocket.holder.WebSocketSessionHolder;
import org.dromara.common.websocket.utils.WebSocketUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.List;

import static org.dromara.common.websocket.constant.WebSocketConstants.LOGIN_USER_KEY;

/**
 * WebSocketHandler 实现类
 *
 * @author zendwang
 */
@Slf4j
public class PlusWebSocketHandler extends AbstractWebSocketHandler {

    private final ICustomWebSocketHandler customWebSocketHandler;

    public PlusWebSocketHandler() {
        this(null);
    }

    public PlusWebSocketHandler(ICustomWebSocketHandler customWebSocketHandler) {
        this.customWebSocketHandler = customWebSocketHandler;
    }

    /**
     * 连接成功后
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        BaseUser user = (BaseUser) session.getAttributes().get(LOGIN_USER_KEY);
        if (user != null) {
            WebSocketSessionHolder.addSession(user.getLoginType(), user.getUserId(), session);
            log.info("[connect] sessionId: {},userId:{},loginType:{},deviceType:{}", session.getId(), user.getUserId(), user.getLoginType(), user.getDeviceType());
            if (customWebSocketHandler != null) {
                customWebSocketHandler.afterConnectionEstablished(session, user);
            }
        }
    }

    /**
     * 处理发送来的文本消息
     *
     * @param session
     * @param message
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        BaseUser user = (BaseUser) session.getAttributes().get(LOGIN_USER_KEY);
        List<Long> userIds = List.of(user.getUserId());
        WebSocketMessageDto webSocketMessageDto = new WebSocketMessageDto();
        webSocketMessageDto.setLoginType(user.getLoginType());
        webSocketMessageDto.setSessionKeys(userIds);
        webSocketMessageDto.setMessage(message.getPayload());
        WebSocketUtils.publishMessage(webSocketMessageDto);

        if (customWebSocketHandler != null) {
            customWebSocketHandler.handleTextMessage(session, message, user);
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);

        if (customWebSocketHandler != null) {
            BaseUser user = (BaseUser) session.getAttributes().get(LOGIN_USER_KEY);
            customWebSocketHandler.handleBinaryMessage(session, message, user);
        }
    }

    /**
     * 心跳监测的回复
     *
     * @param session
     * @param message
     */
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) {
        WebSocketUtils.sendPongMessage(session);
    }

    /**
     * 连接出错时
     *
     * @param session
     * @param exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("[transport error] sessionId: {} , exception:{}", session.getId(), exception.getMessage());

        if (customWebSocketHandler != null) {
            customWebSocketHandler.handleTransportError(session, exception);
        }
    }

    /**
     * 连接关闭后
     *
     * @param session
     * @param status
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        BaseUser user = (BaseUser) session.getAttributes().get(LOGIN_USER_KEY);
        if (user != null) {
            WebSocketSessionHolder.removeSession(user.getLoginType(), user.getUserId(), session);
            log.info("[disconnect] sessionId: {},userId:{},loginType:{},deviceType:{}",
                session.getId(), user.getUserId(), user.getLoginType(), user.getDeviceType());
        }

        if (customWebSocketHandler != null) {
            customWebSocketHandler.afterConnectionClosed(session, status, user);
        }
    }

    /**
     * 是否支持分片消息
     *
     * @return
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
