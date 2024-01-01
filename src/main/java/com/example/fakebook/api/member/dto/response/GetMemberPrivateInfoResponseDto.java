package com.example.fakebook.api.member.dto.response;

import com.example.fakebook.api.member.entity.Member;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public class GetMemberPrivateInfoResponseDto extends GetMemberInfoResponseDto {
    private final LocalDateTime lastSignInAt;
    private final String phoneNumber;

    public static GetMemberPrivateInfoResponseDto from(Member entity) {
        return GetMemberPrivateInfoResponseDto.builder()
                .signId(entity.getSignId())
                .email(entity.getEmail())
                .lastSignInAt(entity.getLastSignInAt())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
}
