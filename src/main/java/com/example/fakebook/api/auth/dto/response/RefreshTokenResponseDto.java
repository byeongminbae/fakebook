package com.example.fakebook.api.auth.dto.response;

import com.example.fakebook.api.auth.entity.RefreshToken;
import com.example.fakebook.global.auth.token.dto.internal.RefreshTokenInternalDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponseDto {
    private final String token;
    private final String expiredAt;

    public static RefreshTokenResponseDto from(RefreshTokenInternalDto refreshTokenInternalDto) {
        return RefreshTokenResponseDto.builder()
                .token(refreshTokenInternalDto.getToken())
                .expiredAt(refreshTokenInternalDto.getExpiredAt().toString())
                .build();
    }

    public static RefreshTokenResponseDto from(RefreshToken entity) {
        return RefreshTokenResponseDto.builder()
                .token(entity.getToken())
                .expiredAt(entity.getExpiredAt().toString())
                .build();
    }
}