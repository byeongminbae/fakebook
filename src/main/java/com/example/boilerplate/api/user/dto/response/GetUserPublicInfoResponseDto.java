package com.example.boilerplate.api.user.dto.response;

import com.example.boilerplate.api.user.entity.UserEntity;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class GetUserPublicInfoResponseDto extends GetUserInfoResponseDto {
    public static GetUserPublicInfoResponseDto from(UserEntity entity) {
        return GetUserPublicInfoResponseDto.builder()
                .signId(entity.getSignId())
                .email(entity.getEmail())
                .build();
    }
}
