package com.example.boilerplate.api.token.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RenewTokenRequestDto {
    @Schema(description = "Refresh token obtained when issuing tokens")
    private String refreshToken;
}
