package com.example.fakebook.global.auth.token.dto.internal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenInternalDto {
    private final AccessTokenInternalDto accessTokenInternalDto;
    private final RefreshTokenInternalDto refreshTokenInternalDto;
}
