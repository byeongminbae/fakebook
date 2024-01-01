package com.example.fakebook.api.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Schema(subTypes = {
        GetMemberPublicInfoResponseDto.class,
        GetMemberPrivateInfoResponseDto.class
})
@SuperBuilder
@Getter
public abstract class GetMemberInfoResponseDto {
    private final String signId;
    private final String email;
}
