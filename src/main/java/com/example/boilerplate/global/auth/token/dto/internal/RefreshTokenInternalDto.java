package com.example.boilerplate.global.auth.token.dto.internal;

import com.example.boilerplate.api.auth.entity.RefreshTokenEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RefreshTokenInternalDto {
    private final String token;
    private final LocalDateTime expiredAt;

    public RefreshTokenEntity to(){
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(token);
        refreshTokenEntity.setExpiredAt(expiredAt);
        return refreshTokenEntity;
    }
}