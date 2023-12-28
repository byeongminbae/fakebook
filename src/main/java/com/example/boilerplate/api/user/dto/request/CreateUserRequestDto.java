package com.example.boilerplate.api.user.dto.request;

import com.example.boilerplate.api.user.entity.UserEntity;
import com.example.boilerplate.global.util.CryptoUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateUserRequestDto {
    private String signId;
    private String signPassword;
    private String email;
    private String phoneNumber;
}
