package com.example.boilerplate.api.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateUserRequestDto {
    @Schema(description = "nickname")
    private String nickname;
    @Schema(description = "email")
    private String email;
}
