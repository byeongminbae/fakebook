package com.example.fakebook.global.enums;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fakebook.global.auth.token.dto.internal.AccessTokenPayloadInternalDto;
import com.example.fakebook.global.auth.token.dto.internal.RefreshTokenPayloadInternalDto;
import com.example.fakebook.global.auth.token.dto.internal.TokenPayloadInternalDto;

public enum TokenType {
    ACCESS {
        @Override
        public AccessTokenPayloadInternalDto getTokenPayloadInternalDto(DecodedJWT decodedJWT) {
            return AccessTokenPayloadInternalDto.builder()
                    .memberId(Long.parseLong(decodedJWT.getClaims().get("memberId").asString()))
                    .role(Role.valueOf(decodedJWT.getClaims().get("role").asString()))
                    .build();
        }
    },
    REFRESH {
        @Override
        public RefreshTokenPayloadInternalDto getTokenPayloadInternalDto(DecodedJWT decodedJWT) {
            return RefreshTokenPayloadInternalDto.builder()
                    .memberId(Long.parseLong(decodedJWT.getClaims().get("memberId").asString()))
                    .build();
        }
    };

    public abstract <T extends TokenPayloadInternalDto> T getTokenPayloadInternalDto(DecodedJWT decodedJWT);
}
