package com.example.boilerplate.api.auth.dto.response;

import com.example.boilerplate.api.auth.entity.RefreshTokenEntity;
import com.example.boilerplate.global.auth.token.dto.internal.RefreshTokenInternalDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RefreshTokenResponseDto {
    private final String token;
    private final LocalDateTime expiredAt;

    public static RefreshTokenResponseDto from(RefreshTokenInternalDto refreshTokenInternalDto) {
        return RefreshTokenResponseDto.builder()
                .token(refreshTokenInternalDto.getToken())
                .expiredAt(refreshTokenInternalDto.getExpiredAt())
                .build();
    }

    public static RefreshTokenResponseDto from(RefreshTokenEntity refreshTokenEntity) {
        return RefreshTokenResponseDto.builder()
                .token(refreshTokenEntity.getRefreshToken())
                .expiredAt(refreshTokenEntity.getExpiredAt())
                .build();
    }
}