package com.example.fakebook.api.member.dto.response;

import com.example.fakebook.api.member.entity.Member;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class GetMemberPublicInfoResponseDto extends GetMemberInfoResponseDto {
    public static GetMemberPublicInfoResponseDto from(Member entity) {
        return GetMemberPublicInfoResponseDto.builder()
                .signId(entity.getSignId())
                .email(entity.getEmail())
                .build();
    }
}
