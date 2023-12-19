package com.example.boilerplate.api.token.dto.response;

import com.example.boilerplate.global.auth.dto.internal.RefreshTokenInternalDto;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class RefreshTokenResponseDto {
    private final String token;
    private final Date expiredAt;

    public static RefreshTokenResponseDto from(RefreshTokenInternalDto refreshTokenInternalDto) {
        return RefreshTokenResponseDto.builder()
                .token(refreshTokenInternalDto.getToken())
                .expiredAt(refreshTokenInternalDto.getExpiredAt())
                .build();
    }
}