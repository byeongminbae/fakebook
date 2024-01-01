package com.example.fakebook.api.member.dto.request;

import lombok.Getter;

@Getter
public class UpdateMemberRequestDto {
    private String email;
    private String phoneNumber;
}
