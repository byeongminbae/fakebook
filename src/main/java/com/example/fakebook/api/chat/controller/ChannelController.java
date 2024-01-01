package com.example.fakebook.api.chat.controller;

import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Channel Controller", description = "Channel Controller")
@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {
    @Operation(summary = "")
    @GetMapping
    public SuccessVoidResponseDto getChannels() {
        return null;
    }

    @Operation(summary = "")
    @PostMapping
    public SuccessVoidResponseDto createChannel() {
        return null;
    }

    @Operation(summary = "")
    @DeleteMapping("/{channelId}")
    public SuccessVoidResponseDto deleteChannel() {
        return null;
    }



    @Operation(summary = "")
    @PostMapping("/{channelId}/members/{memberId}")
    public SuccessVoidResponseDto joinChannel() {
        return null;
    }

    @Operation(summary = "")
    @DeleteMapping("/{channelId}/members/{memberId}")
    public SuccessVoidResponseDto exitChannel() {
        return null;
    }

}
