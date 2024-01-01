package com.example.fakebook.api.chat.controller;

import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat Controller", description = "Chat Controller")
@RestController
@RequestMapping("/channel/{channelId}/chat")
@RequiredArgsConstructor
public class ChatController {
    @Operation(summary = "")
    @GetMapping
    public SuccessVoidResponseDto getChats() {
        return null;
    }
}
