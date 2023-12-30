package com.example.fakebook.api.user.controller;

import com.example.fakebook.api.user.dto.request.CreateUserRequestDto;
import com.example.fakebook.api.user.dto.request.UpdateUserPasswordRequestDto;
import com.example.fakebook.api.user.dto.request.UpdateUserRequestDto;
import com.example.fakebook.api.user.dto.response.CreateUserResponseDto;
import com.example.fakebook.api.user.dto.response.GetUserInfoResponseDto;
import com.example.fakebook.api.user.service.UserService;
import com.example.fakebook.global.auth.aop.Auth;
import com.example.fakebook.global.dto.response.SuccessResponseDto;
import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import com.example.fakebook.global.entity.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "User Controller")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "User creation. Password encrypted with sha512.")
    @PostMapping
    public SuccessResponseDto<CreateUserResponseDto> createUser(
            @RequestBody CreateUserRequestDto createUserRequestDto
    ) {
        return userService.createUser(createUserRequestDto);
    }

    @Operation(
            summary = "User retrieval. Token required for detailed self-retrieval",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/{userId}")
    public SuccessResponseDto<GetUserInfoResponseDto> getUser(
            @PathVariable Long userId,
            @Schema(hidden = true)
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        return userService.getUser(userId, authorizationHeader);
    }

    @Auth(userRoles = {UserRole.USER, UserRole.ADMIN}, pathVariableUserId = "userId")
    @Operation(
            summary = "User update. Fields are optional",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PatchMapping("/{userId}")
    public SuccessVoidResponseDto updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequestDto updateUserRequestDto
    ) {
        return userService.updateUser(userId, updateUserRequestDto);
    }

    @Auth(userRoles = {UserRole.USER, UserRole.ADMIN}, pathVariableUserId = "userId")
    @Operation(
            summary = "User password update",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PatchMapping("/{userId}/password")
    public SuccessVoidResponseDto updateUserPassword(
            @PathVariable Long userId,
            @RequestBody UpdateUserPasswordRequestDto updateUserPasswordRequestDto
    ) {
        return userService.updateUserPassword(userId, updateUserPasswordRequestDto);
    }

    @Auth(userRoles = {UserRole.USER, UserRole.ADMIN}, pathVariableUserId = "userId")
    @Operation(
            summary = "User logical deletion",
            security = @SecurityRequirement(name = "Authorization")
    )
    @DeleteMapping("/{userId}")
    public SuccessVoidResponseDto deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }
}