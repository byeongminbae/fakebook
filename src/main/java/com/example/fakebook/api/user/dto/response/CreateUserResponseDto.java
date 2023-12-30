package com.example.fakebook.api.user.dto.response;

import com.example.fakebook.api.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserResponseDto {
    private Long userId;

    public static CreateUserResponseDto from(UserEntity entity) {
        return CreateUserResponseDto.builder()
                .userId(entity.getId())
                .build();
    }
}
