package org.dromara.common.websocket.holder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketSession 用于保存当前所有在线的会话信息
 *
 * @author zendwang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketSessionHolder {

    private static final Map<String, Map<Long, Set<WebSocketSession>>> USER_SESSION_MAP = new ConcurrentHashMap<>();

    public static void addSession(String loginType, Long sessionKey, WebSocketSession session) {
        if (session == null) {
            return;
        }
        Map<Long, Set<WebSocketSession>> map = USER_SESSION_MAP.computeIfAbsent(loginType, s -> new ConcurrentHashMap<>());

        Set<WebSocketSession> sessions = map.computeIfAbsent(sessionKey, aLong -> new LinkedHashSet<>());
        sessions.add(session);
    }

    public static void removeSession(String loginType, Long sessionKey, WebSocketSession session) {
        Map<Long, Set<WebSocketSession>> map = USER_SESSION_MAP.get(loginType);
        if (map == null) {
            return;
        }
        Set<WebSocketSession> sessions = map.get(sessionKey);
        if (sessions == null) {
            return;
        }
        sessions.remove(session);
    }

    public static void removeSession(String loginType, Long sessionKey, String sessionId) {
        Map<Long, Set<WebSocketSession>> map = USER_SESSION_MAP.get(loginType);
        if (map == null) {
            return;
        }
        Set<WebSocketSession> sessions = map.get(sessionKey);
        if (sessions == null) {
            return;
        }
        sessions.removeIf(webSocketSession -> webSocketSession.getId().equals(sessionId));
    }

    public static void removeSession(String loginType, Long sessionKey) {
        Map<Long, Set<WebSocketSession>> map = USER_SESSION_MAP.get(loginType);
        if (map != null) {
            map.remove(sessionKey);
        }
    }

    public static Set<WebSocketSession> getSessions(String loginType, Long sessionKey) {
        Map<Long, Set<WebSocketSession>> map = USER_SESSION_MAP.get(loginType);
        if (map != null) {
            return map.get(sessionKey);
        }
        return null;
    }

    public static Set<Long> getSessionsAll(String loginType) {
        return Optional.ofNullable(USER_SESSION_MAP.get(loginType)).map(Map::keySet).orElse(null);
    }

    public static Boolean existSession(String loginType, Long sessionKey) {
        return Optional.ofNullable(USER_SESSION_MAP.get(loginType)).map(map -> map.containsKey(sessionKey)).orElse(false);
    }
}
