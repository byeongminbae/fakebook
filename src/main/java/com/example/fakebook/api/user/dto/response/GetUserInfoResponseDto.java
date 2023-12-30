package com.example.fakebook.api.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Schema(subTypes = {
        GetUserPublicInfoResponseDto.class,
        GetUserPrivateInfoResponseDto.class
})
@SuperBuilder
@Getter
public abstract class GetUserInfoResponseDto {
    private final String signId;
    private final String email;
}
