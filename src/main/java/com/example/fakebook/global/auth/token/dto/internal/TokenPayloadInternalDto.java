package com.example.fakebook.global.auth.token.dto.internal;

import com.example.fakebook.global.enums.TokenType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class TokenPayloadInternalDto {
    private Long userId;
    private TokenType tokenType;
}
