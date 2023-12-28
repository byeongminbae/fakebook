package com.example.boilerplate.api.user.dto.response;

import com.example.boilerplate.api.user.entity.UserEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public class GetUserPrivateInfoResponseDto extends GetUserInfoResponseDto {
    private final LocalDateTime lastSignInAt;
    private final String phoneNumber;

    public static GetUserPrivateInfoResponseDto from(UserEntity entity) {
        return GetUserPrivateInfoResponseDto.builder()
                .signId(entity.getSignId())
                .email(entity.getEmail())
                .lastSignInAt(entity.getLastSignInAt())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}
