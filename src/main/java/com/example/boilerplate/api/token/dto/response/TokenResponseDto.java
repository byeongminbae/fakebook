package com.example.boilerplate.api.token.dto.response;

import com.example.boilerplate.global.auth.dto.internal.AccessTokenInternalDto;
import com.example.boilerplate.global.auth.dto.internal.RefreshTokenInternalDto;
import com.example.boilerplate.global.auth.dto.internal.TokenInternalDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {
    private final AccessTokenResponseDto accessTokenResponseDto;
    private final RefreshTokenResponseDto refreshTokenResponseDto;

    public static TokenResponseDto from(TokenInternalDto tokenInternalDto){
        AccessTokenInternalDto accessTokenInternalDto = tokenInternalDto.getAccessTokenInternalDto();
        RefreshTokenInternalDto refreshTokenInternalDto = tokenInternalDto.getRefreshTokenInternalDto();

        AccessTokenResponseDto buildedAccessTokenResponseDto = AccessTokenResponseDto.from(accessTokenInternalDto);
        RefreshTokenResponseDto buildedRefreshTokenResponseDto = RefreshTokenResponseDto.from(refreshTokenInternalDto);

        return TokenResponseDto.builder()
                .accessTokenResponseDto(buildedAccessTokenResponseDto)
                .refreshTokenResponseDto(buildedRefreshTokenResponseDto)
                .build();
    }
}
