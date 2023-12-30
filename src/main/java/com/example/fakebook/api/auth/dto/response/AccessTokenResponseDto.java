package com.example.fakebook.api.auth.dto.response;

import com.example.fakebook.global.auth.token.dto.internal.AccessTokenInternalDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccessTokenResponseDto {
    private final String token;
    private final LocalDateTime expiredAt;

    public static AccessTokenResponseDto from(AccessTokenInternalDto accessTokenInternalDto) {
        return AccessTokenResponseDto.builder()
                .token(accessTokenInternalDto.getToken())
                .expiredAt(accessTokenInternalDto.getExpiredAt())
                .build();
    }
}