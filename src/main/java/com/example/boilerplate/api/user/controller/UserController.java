package com.example.boilerplate.api.user.controller;

import com.example.boilerplate.api.user.dto.request.CreateUserRequestDto;
import com.example.boilerplate.api.user.dto.request.ReplaceUserRequestDto;
import com.example.boilerplate.api.user.dto.request.UpdateUserRequestDto;
import com.example.boilerplate.api.user.dto.response.CreateUserResponseDto;
import com.example.boilerplate.api.user.dto.response.GetUserResponseDto;
import com.example.boilerplate.api.user.dto.response.ReplaceUserResponseDto;
import com.example.boilerplate.api.user.dto.response.UpdateUserResponseDto;
import com.example.boilerplate.api.user.service.UserService;
import com.example.boilerplate.global.aop.Auth;
import com.example.boilerplate.global.dto.response.SuccessResponseDto;
import com.example.boilerplate.global.entity.UserRole;
import io.swagger.v3.oas.annotations.Operation;
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

    @Auth(userRoles = {UserRole.ADMIN, UserRole.USER}, requestParamUserId = "userId")
    @Operation(summary = "Get user", security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("/{userId}")
    public SuccessResponseDto<GetUserResponseDto> getUser(@RequestParam Long userId) {
        return userService.getUser(userId);
    }

    @Auth(userRoles = {UserRole.ADMIN, UserRole.USER}, requestParamUserId = "userId")
    @Operation(summary = "Update user", security = @SecurityRequirement(name = "Authorization"))
    @PatchMapping("/{userId}")
    public SuccessResponseDto<UpdateUserResponseDto> updateUser(
            @RequestParam Long userId,
            @RequestBody UpdateUserRequestDto updateUserRequestDto
    ) {
        return userService.updateUser(userId, updateUserRequestDto);
    }

    @Auth(userRoles = {UserRole.ADMIN, UserRole.USER}, requestParamUserId = "userId")
    @Operation(summary = "Replace user", security = @SecurityRequirement(name = "Authorization"))
    @PutMapping("/{userId}")
    public SuccessResponseDto<ReplaceUserResponseDto> replaceUser(
            @RequestParam Long userId,
            @RequestBody ReplaceUserRequestDto replaceUserRequestDto
    ) {
        return userService.replaceUser(userId, replaceUserRequestDto);
    }

    @Operation(summary = "Create user")
    @PostMapping
    public SuccessResponseDto<CreateUserResponseDto> createUser(
            @RequestBody CreateUserRequestDto createUserRequestDto
    ) {
        return userService.createUser(createUserRequestDto);
    }

    @Auth(userRoles = {UserRole.ADMIN, UserRole.USER}, requestParamUserId = "userId")
    @Operation(summary = "Delete user", security = @SecurityRequirement(name = "Authorization"))
    @DeleteMapping("/{userId}")
    public SuccessResponseDto<Object> deleteUser(@RequestParam Long userId) {
        return userService.deleteUser(userId);
    }
}