package com.example.fakebook.global.auth.token.dto.internal;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccessTokenInternalDto {
    private final String token;
    private final LocalDateTime expiredAt;
}
