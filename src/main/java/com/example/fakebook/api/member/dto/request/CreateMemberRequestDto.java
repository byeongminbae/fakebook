package com.example.fakebook.api.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateMemberRequestDto {
    @Schema(example = "myId")
    private String signId;
    @Schema(example = "myPassword")
    private String signPassword;
    @Schema(example = "my@email.com")
    private String email;
    @Schema(example = "+82-10-0000-0000")
    private String phoneNumber;
}
