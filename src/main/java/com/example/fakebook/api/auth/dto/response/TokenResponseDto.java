package com.example.fakebook.api.auth.dto.response;


import com.example.fakebook.global.auth.token.dto.internal.AccessTokenInternalDto;
import com.example.fakebook.global.auth.token.dto.internal.RefreshTokenInternalDto;
import com.example.fakebook.global.auth.token.dto.internal.TokenInternalDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {
    private final AccessTokenResponseDto accessTokenResponseDto;
    private final RefreshTokenResponseDto refreshTokenResponseDto;

    public static TokenResponseDto from(TokenInternalDto tokenInternalDto) {
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