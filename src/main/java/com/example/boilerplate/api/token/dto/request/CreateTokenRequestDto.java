package com.example.boilerplate.api.token.dto.request;

import com.example.boilerplate.global.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateTokenRequestDto {
    @Schema(description = "userId granted from user creation api", example = "1")
    private Long userId;
    @Schema(description = "Roles to be granted to users temporarily")
    private UserRole userRole;
}
