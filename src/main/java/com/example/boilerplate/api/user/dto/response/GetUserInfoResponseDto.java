package com.example.boilerplate.api.user.dto.response;

import com.example.boilerplate.api.user.dto.response.GetUserPrivateInfoResponseDto;
import com.example.boilerplate.api.user.dto.response.GetUserPublicInfoResponseDto;
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
