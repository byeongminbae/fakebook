package com.example.fakebook.api.chat.handler;

import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {
    private static final Map<String, List<WebSocketSession>> channel = new ConcurrentHashMap<>();

    private static Long getChannelId(WebSocketSession webSocketSession) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(String.valueOf(webSocketSession.getUri()))
                .build();

        String channelId = uriComponents.getQueryParams().getFirst("channelId");
        if (Objects.isNull(channelId)) throw new BusinessException(CommonException.WEBSOCKET_NOT_FOUND_EXCEPTION);

        return Long.valueOf(channelId);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws IOException {
        Long channelId = getChannelId(webSocketSession);

        System.out.println(webSocketSession.getAttributes());
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable exception) {
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        Long channelId = getChannelId(webSocketSession);

    }
}
