package com.example.fakebook.api.member.controller;

import com.example.fakebook.api.member.dto.request.CreateMemberRequestDto;
import com.example.fakebook.api.member.dto.request.UpdateMemberPasswordRequestDto;
import com.example.fakebook.api.member.dto.request.UpdateMemberRequestDto;
import com.example.fakebook.api.member.dto.response.CreateMemberResponseDto;
import com.example.fakebook.api.common.dto.response.GetChannelResponseDto;
import com.example.fakebook.api.common.dto.response.GetChatResponseDto;
import com.example.fakebook.api.member.dto.response.GetMemberInfoResponseDto;
import com.example.fakebook.api.member.service.MemberService;
import com.example.fakebook.global.auth.aop.Auth;
import com.example.fakebook.global.dto.response.SuccessDataResponseDto;
import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import com.example.fakebook.global.enums.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member Controller", description = "Member Controller")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "Member creation. Password encrypted with sha512.")
    @PostMapping
    public SuccessDataResponseDto<CreateMemberResponseDto> createMember(
            @RequestBody CreateMemberRequestDto createMemberRequestDto
    ) {
        return memberService.createMember(createMemberRequestDto);
    }

    @Operation(
            summary = "Member retrieval. Token required for detailed self-retrieval",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/{memberId}")
    public SuccessDataResponseDto<GetMemberInfoResponseDto> getMember(
            @PathVariable Long memberId,
            @Schema(hidden = true)
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        return memberService.getMember(memberId, authorizationHeader);
    }

    @Auth(memberId = {Role.USER, Role.ADMIN}, pathVariableMemberIdFieldName = "memberId")
    @Operation(
            summary = "Member update. Fields are optional",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PatchMapping("/{memberId}")
    public SuccessVoidResponseDto updateMember(
            @PathVariable Long memberId,
            @RequestBody UpdateMemberRequestDto updateMemberRequestDto
    ) {
        return memberService.updateMember(memberId, updateMemberRequestDto);
    }

    @Auth(memberId = {Role.USER, Role.ADMIN}, pathVariableMemberIdFieldName = "memberId")
    @Operation(
            summary = "Member password update",
            security = @SecurityRequirement(name = "Authorization")
    )
    @PatchMapping("/{memberId}/password")
    public SuccessVoidResponseDto updateMemberPassword(
            @PathVariable Long memberId,
            @RequestBody UpdateMemberPasswordRequestDto updateMemberPasswordRequestDto
    ) {
        return memberService.updateMemberPassword(memberId, updateMemberPasswordRequestDto);
    }

    @Auth(memberId = {Role.USER, Role.ADMIN}, pathVariableMemberIdFieldName = "memberId")
    @Operation(
            summary = "Member logical deletion",
            security = @SecurityRequirement(name = "Authorization")
    )
    @DeleteMapping("/{memberId}")
    public SuccessVoidResponseDto deleteMember(@PathVariable Long memberId) {
        return memberService.deleteMember(memberId);
    }

    @Operation(summary = "")
    @GetMapping("/{memberId}/friends")
    public SuccessVoidResponseDto getFriends() {
        return null;
    }

    @Operation(summary = "")
    @PostMapping("/{memberId}/friends")
    public SuccessVoidResponseDto addFriend() {
        return null;
    }

    @Operation(summary = "")
    @DeleteMapping("/{memberId}/friends")
    public SuccessVoidResponseDto deleteFriend() {
        return null;
    }

    @Auth(memberId = {Role.USER, Role.ADMIN}, pathVariableMemberIdFieldName = "memberId")
    @Operation(
            summary = "Retrieve channels I'm participating in",
            security = @SecurityRequirement(name = "Authorization")
    )
    @GetMapping("/{memberId}/channels")
    public SuccessDataResponseDto<List<GetChannelResponseDto>> getChannels(@PathVariable Long memberId) {
        return memberService.getChannels(memberId);
    }

    @Auth(memberId = {Role.USER, Role.ADMIN}, pathVariableMemberIdFieldName = "memberId")
    @Operation(summary = "Get all chats", security = @SecurityRequirement(name = "Authorization"))
    @GetMapping("/{memberId}/channels/{channelId}/chats")
    public SuccessDataResponseDto<List<GetChatResponseDto>> getChats(
            @PathVariable Long memberId,
            @PathVariable Long channelId
    ) {
        return memberService.getChats(memberId, channelId);
    }
}