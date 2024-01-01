package com.example.fakebook.api.media.controller;

import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Media Controller", description = "Media Controller")
@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {
    @Operation(summary = "")
    @PostMapping
    public SuccessVoidResponseDto uploadMedia() {
        return null;
    }
}
