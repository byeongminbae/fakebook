package com.example.fakebook.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TokenSignInRequestDto {
    @Schema(example = "myId")
    private String signId;
    @Schema(example = "myPassword")
    private String signPassword;
}
