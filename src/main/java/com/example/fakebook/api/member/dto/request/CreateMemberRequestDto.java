package com.example.fakebook.api.member.dto.request;

import lombok.Getter;

@Getter
public class CreateMemberRequestDto {
    private String signId;
    private String signPassword;
    private String email;
    private String phoneNumber;
}
