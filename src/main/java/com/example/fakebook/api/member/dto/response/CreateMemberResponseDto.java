package com.example.fakebook.api.member.dto.response;

import com.example.fakebook.api.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateMemberResponseDto {
    private Long memberId;

    public static CreateMemberResponseDto from(Member entity) {
        return CreateMemberResponseDto.builder()
                .memberId(entity.getId())
                .build();
    }
}
