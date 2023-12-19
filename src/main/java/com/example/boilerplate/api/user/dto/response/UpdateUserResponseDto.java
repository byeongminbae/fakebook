package com.example.boilerplate.api.user.dto.response;

import com.example.boilerplate.api.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateUserResponseDto {
    @Schema(description = "nickname")
    private final String nickname;
    @Schema(description = "email")
    private final String email;
    @Schema(description = "createdAt")
    private final LocalDateTime createdAt;
    @Schema(description = "updatedAt")
    private final LocalDateTime updatedAt;

    public static UpdateUserResponseDto from(UserEntity entity) {
        return UpdateUserResponseDto.builder()
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}