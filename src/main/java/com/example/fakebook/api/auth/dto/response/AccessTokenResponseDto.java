package com.example.fakebook.api.auth.dto.response;

import com.example.fakebook.global.auth.token.dto.internal.AccessTokenInternalDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessTokenResponseDto {
    private final String token;
    private final String expiredAt;

    public static AccessTokenResponseDto from(AccessTokenInternalDto accessTokenInternalDto) {
        return AccessTokenResponseDto.builder()
                .token(accessTokenInternalDto.getToken())
                .expiredAt(accessTokenInternalDto.getExpiredAt().toString())
                .build();
    }
}