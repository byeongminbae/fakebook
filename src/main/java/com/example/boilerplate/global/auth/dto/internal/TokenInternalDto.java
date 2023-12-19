package com.example.boilerplate.global.auth.dto.internal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenInternalDto {
    private final AccessTokenInternalDto accessTokenInternalDto;
    private final RefreshTokenInternalDto refreshTokenInternalDto;
}
