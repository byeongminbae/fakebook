package com.example.fakebook.api.chat.handler;

import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.entity.Chat;
import com.example.fakebook.api.chat.repository.ChannelRepository;
import com.example.fakebook.api.chat.repository.ChatRepository;
import com.example.fakebook.api.common.dto.response.GetChatResponseDto;
import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.api.member.repository.MemberRepository;
import com.example.fakebook.global.auth.token.TokenManager;
import com.example.fakebook.global.auth.token.dto.internal.AccessTokenPayloadInternalDto;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {
    private final TokenManager tokenManager;
    private final ChannelRepository channelRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private final ChatRepository chatRepository;
    private static final Map<Long, List<WebSocketSession>> webSocketSessionMap = new ConcurrentHashMap<>();

    private static Long getChannelId(WebSocketSession webSocketSession) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(String.valueOf(webSocketSession.getUri())).build();

        String channelId = uriComponents.getQueryParams().getFirst("channelId");
        if (Objects.isNull(channelId)) throw new BusinessException(CommonException.CHAT_NOT_FOUND_EXCEPTION);

        return Long.valueOf(channelId);
    }


    private static void sendMessage(WebSocketSession webSocketSession, String content) {
        try {
            webSocketSession.sendMessage(new TextMessage(content));
        } catch (IOException exception) {
            throw new BusinessException(CommonException.CHAT_CANNOT_SEND_MESSAGE);
        }
    }

    private <T> void sendJsonMessage(WebSocketSession webSocketSession, T content) {
        try {
            sendMessage(webSocketSession, objectMapper.writeValueAsString(content));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Long getMemberId(WebSocketSession webSocketSession) {
        String authorization = webSocketSession.getHandshakeHeaders().getFirst("Authorization");
        AccessTokenPayloadInternalDto accessTokenPayloadInternalDto = tokenManager.decodeToken(authorization);
        return accessTokenPayloadInternalDto.getMemberId();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        Long memberId = getMemberId(webSocketSession);
        Long channelId = getChannelId(webSocketSession);

        channelRepository.findByIdAndChannelMembersMemberIdAndDeletedAtIsNullThrowIfNull(channelId, memberId);

        if (webSocketSessionMap.containsKey(channelId)) {
            if (!webSocketSessionMap.get(channelId).contains(webSocketSession)) {
                webSocketSessionMap.get(channelId).add(webSocketSession);
            }
            return;
        }

        List<WebSocketSession> webSocketSessions = new ArrayList<>();
        webSocketSessions.add(webSocketSession);
        webSocketSessionMap.put(channelId, webSocketSessions);
    }

    @Transactional
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        Long memberId = getMemberId(webSocketSession);
        Long channelId = getChannelId(webSocketSession);

        Channel channel = channelRepository.findByIdAndChannelMembersMemberIdAndDeletedAtIsNullThrowIfNull(
                channelId,
                memberId
        );

        Member member = memberRepository.findByIdAndDeletedAtIsNullThrowIfNull(memberId);

        Chat chat = new Chat();
        chat.setContent(webSocketMessage.getPayload().toString());
        chat.setAuthor(member);

        channel.addChat(chat);
        channelRepository.save(channel);

        Chat savedChat = chatRepository.findByIdAndDeletedAtIsNullThrowIfNull(chat.getId());

        webSocketSessionMap.get(channelId).forEach((session) -> {
            if (!webSocketSession.equals(session)) sendJsonMessage(session, GetChatResponseDto.from(savedChat));
        });

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
        if (webSocketSessionMap.containsKey(channelId)) webSocketSessionMap.get(channelId).remove(webSocketSession);
    }
}
