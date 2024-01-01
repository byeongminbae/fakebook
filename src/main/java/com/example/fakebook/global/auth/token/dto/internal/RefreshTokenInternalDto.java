package com.example.fakebook.global.auth.token.dto.internal;

import com.example.fakebook.api.auth.entity.RefreshToken;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RefreshTokenInternalDto {
    private final String token;
    private final LocalDateTime expiredAt;

    public RefreshToken to() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setExpiredAt(expiredAt);
        return refreshToken;
    }
}