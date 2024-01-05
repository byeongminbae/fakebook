package com.example.fakebook.api.auth.controller;

import com.example.fakebook.api.auth.dto.request.TokenRenewRequestDto;
import com.example.fakebook.api.auth.dto.request.TokenSignInRequestDto;
import com.example.fakebook.api.auth.dto.request.TokenSignOutRequestDto;
import com.example.fakebook.api.auth.dto.response.AccessTokenResponseDto;
import com.example.fakebook.api.auth.dto.response.RefreshTokenResponseDto;
import com.example.fakebook.api.auth.dto.response.TokenResponseDto;
import com.example.fakebook.api.auth.service.AuthService;
import com.example.fakebook.global.auth.aop.Auth;
import com.example.fakebook.global.dto.response.SuccessResponseDto;
import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import com.example.fakebook.global.enums.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Auth Controller", description = "Auth Controller")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Sign in with token-based authentication")
    @PostMapping("/token/sign-in")
    public SuccessResponseDto<TokenResponseDto> tokenSignIn(
            @RequestBody TokenSignInRequestDto tokenSignInRequestDto
    ) {
        return authService.tokenSignIn(tokenSignInRequestDto);
    }

    @Auth(memberId = {Role.USER, Role.ADMIN})
    @Operation(summary = "Sign out with token-based authentication", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping("/token/sign-out")
    public SuccessVoidResponseDto tokenSignOut(
            @RequestBody TokenSignOutRequestDto tokenSignOutRequestDto
    ) {
        return authService.tokenSignOut(tokenSignOutRequestDto);
    }

    @Operation(summary = "Renew access token using refresh token")
    @PostMapping("/token/renew")
    public SuccessResponseDto<AccessTokenResponseDto> tokenRenew(
            @RequestBody TokenRenewRequestDto tokenRenewRequestDto
    ) {
        return authService.tokenRenew(tokenRenewRequestDto);
    }

    @Auth(memberId = {Role.USER, Role.ADMIN})
    @Operation(summary = "Get refresh token list", security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("/token/refresh")
    public SuccessResponseDto<List<RefreshTokenResponseDto>> getRefreshTokenList(
            @Schema(hidden = true)
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        return authService.getRefreshTokenList(authorizationHeader);
    }
}
