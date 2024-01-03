package com.example.fakebook.api.chat.controller;

import com.example.fakebook.api.chat.dto.request.CreateChannelRequestDto;
import com.example.fakebook.api.chat.dto.response.CreateChannelResponseDto;
import com.example.fakebook.api.chat.service.ChannelService;
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

@Tag(name = "Channel Controller", description = "Channel Controller")
@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @Operation(summary = "")
    @GetMapping
    public SuccessVoidResponseDto getChannels() {
        return channelService.getChannels();
    }

    @Auth(memberId = {Role.USER, Role.ADMIN})
    @Operation(summary = "Create channel", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping
    public SuccessResponseDto<CreateChannelResponseDto> createChannel(
            @Schema(hidden = true)
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody CreateChannelRequestDto createChannelRequestDto
    ) {
         return channelService.createChannel(authorizationHeader, createChannelRequestDto);
    }

    @Operation(summary = "")
    @DeleteMapping("/{channelId}")
    public SuccessVoidResponseDto deleteChannel() {
        return channelService.deleteChannel();
    }

    @Operation(summary = "")
    @PostMapping("/{channelId}/members/{memberId}")
    public SuccessVoidResponseDto joinChannel() {
        return channelService.joinChannel();
    }

    @Operation(summary = "")
    @DeleteMapping("/{channelId}/members/{memberId}")
    public SuccessVoidResponseDto exitChannel() {
        return channelService.exitChannel();
    }

}
