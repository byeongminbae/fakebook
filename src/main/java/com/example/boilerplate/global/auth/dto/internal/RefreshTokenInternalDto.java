package com.example.boilerplate.global.auth.dto.internal;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class RefreshTokenInternalDto {
    private final String token;
    private final Date expiredAt;
}
