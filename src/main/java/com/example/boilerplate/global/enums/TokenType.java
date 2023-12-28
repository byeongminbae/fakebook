package com.example.boilerplate.global.enums;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.boilerplate.global.auth.token.dto.internal.AccessTokenPayloadInternalDto;
import com.example.boilerplate.global.auth.token.dto.internal.RefreshTokenPayloadInternalDto;
import com.example.boilerplate.global.auth.token.dto.internal.TokenPayloadInternalDto;
import com.example.boilerplate.global.entity.UserRole;

public enum TokenType {
    ACCESS {
        @Override
        public AccessTokenPayloadInternalDto getTokenPayloadInternalDto(DecodedJWT decodedJWT) {
            return AccessTokenPayloadInternalDto.builder()
                    .userId(Long.parseLong(decodedJWT.getClaims().get("userId").asString()))
                    .userRole(UserRole.valueOf(decodedJWT.getClaims().get("userRole").asString()))
                    .build();
        }
    },
    REFRESH {
        @Override
        public RefreshTokenPayloadInternalDto getTokenPayloadInternalDto(DecodedJWT decodedJWT) {
            return RefreshTokenPayloadInternalDto.builder()
                    .userId(Long.parseLong(decodedJWT.getClaims().get("userId").asString()))
                    .build();
        }
    };

    public abstract <T extends TokenPayloadInternalDto> T getTokenPayloadInternalDto(DecodedJWT decodedJWT);
}
