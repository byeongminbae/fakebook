package com.example.boilerplate.api.user.dto.response;

import com.example.boilerplate.api.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserResponseDto {
    @Schema(description = "userId")
    private final Long userId;

    public static CreateUserResponseDto from(UserEntity entity) {
        return CreateUserResponseDto.builder()
                .userId(entity.getId())
                .build();
    }
}