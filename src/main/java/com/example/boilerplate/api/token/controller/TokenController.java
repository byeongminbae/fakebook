package com.example.boilerplate.api.token.controller;

import com.example.boilerplate.api.token.dto.request.CreateTokenRequestDto;
import com.example.boilerplate.api.token.dto.request.RenewTokenRequestDto;
import com.example.boilerplate.api.token.dto.response.AccessTokenResponseDto;
import com.example.boilerplate.api.token.dto.response.TokenResponseDto;
import com.example.boilerplate.api.token.service.TokenService;
import com.example.boilerplate.global.dto.response.SuccessResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Token Controller", description = "Token Controller")
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @Operation(summary = "Create token for testing")
    @PostMapping
    public SuccessResponseDto<TokenResponseDto> createToken(
            @RequestBody CreateTokenRequestDto createTokenRequestDto
    ) {
        return tokenService.createToken(createTokenRequestDto);
    }

    @Operation(summary = "Create access tokens from refresh tokens")
    @PostMapping("/renew")
    public SuccessResponseDto<AccessTokenResponseDto> renewToken(
            @RequestBody RenewTokenRequestDto renewTokenRequestDto
    ) {
        return tokenService.renewToken(renewTokenRequestDto);
    }
}