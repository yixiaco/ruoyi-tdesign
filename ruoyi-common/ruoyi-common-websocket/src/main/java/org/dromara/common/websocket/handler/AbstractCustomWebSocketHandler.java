package org.dromara.common.websocket.handler;

import org.dromara.common.core.domain.model.BaseUser;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 自定义websocket处理器
 *
 * @author hexm
 * @date 2024/03/07 15:12
 */
public class AbstractCustomWebSocketHandler implements ICustomWebSocketHandler {

    /**
     * 连接成功后
     */
    public void afterConnectionEstablished(WebSocketSession session, BaseUser user) {
    }

    /**
     * 处理发送来的文本消息
     *
     * @param session
     * @param message
     */
    public void handleTextMessage(WebSocketSession session, TextMessage message, BaseUser user) {
    }

    /**
     * 处理接收的二进制消息
     *
     * @param session
     * @param message
     * @param user
     */
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message, BaseUser user) {
    }

    /**
     * 心跳监测的回复
     *
     * @param session
     * @param message
     */
    public void handlePongMessage(WebSocketSession session, PongMessage message) {
    }

    /**
     * 连接出错时
     *
     * @param session
     * @param exception
     */
    public void handleTransportError(WebSocketSession session, Throwable exception) {
    }

    /**
     * 连接关闭后
     *
     * @param session
     * @param status
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status, BaseUser user) {
    }
}
