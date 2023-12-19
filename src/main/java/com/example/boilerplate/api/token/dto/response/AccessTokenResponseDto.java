package com.example.boilerplate.api.token.dto.response;

import com.example.boilerplate.global.auth.dto.internal.AccessTokenInternalDto;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class AccessTokenResponseDto {
    private final String token;
    private final Date expiredAt;

    public static AccessTokenResponseDto from(AccessTokenInternalDto accessTokenInternalDto){
        return AccessTokenResponseDto.builder()
                .token(accessTokenInternalDto.getToken())
                .expiredAt(accessTokenInternalDto.getExpiredAt())
                .build();
    }
}
