package org.dromara.common.websocket.holder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketSession 用于保存当前所有在线的会话信息
 *
 * @author zendwang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebSocketSessionHolder {

    private static final Map<Long, Set<WebSocketSession>> USER_SESSION_MAP = new ConcurrentHashMap<>();

    public static void addSession(Long sessionKey, WebSocketSession session) {
        if (USER_SESSION_MAP.containsKey(sessionKey)) {
            Set<WebSocketSession> sessions = USER_SESSION_MAP.get(sessionKey);
            sessions.add(session);
        } else {
            Set<WebSocketSession> sessions = new LinkedHashSet<>();
            sessions.add(session);
            USER_SESSION_MAP.put(sessionKey, sessions);
        }
    }

    public static void removeSession(Long sessionKey, WebSocketSession session) {
        if (USER_SESSION_MAP.containsKey(sessionKey)) {
            USER_SESSION_MAP.get(sessionKey).remove(session);
        }
    }

    public static void removeSession(Long sessionKey, String sessionId) {
        if (USER_SESSION_MAP.containsKey(sessionKey)) {
            USER_SESSION_MAP.get(sessionKey)
                .removeIf(webSocketSession -> webSocketSession.getId().equals(sessionId));
        }
    }

    public static void removeSession(Long sessionKey) {
        USER_SESSION_MAP.remove(sessionKey);
    }

    public static Set<WebSocketSession> getSessions(Long sessionKey) {
        return USER_SESSION_MAP.get(sessionKey);
    }

    public static Set<Long> getSessionsAll() {
        return USER_SESSION_MAP.keySet();
    }

    public static Boolean existSession(Long sessionKey) {
        return USER_SESSION_MAP.containsKey(sessionKey);
    }
}
