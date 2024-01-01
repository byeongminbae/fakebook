package com.example.fakebook.api.feed.controller;

import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Feed Controller", description = "Feed Controller")
@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {
    @Operation(summary = "")
    @GetMapping
    public SuccessVoidResponseDto getFeeds() {
        return null;
    }

    @Operation(summary = "")
    @PostMapping
    public SuccessVoidResponseDto createFeed() {
        return null;
    }

    @Operation(summary = "")
    @GetMapping("/{feedId}")
    public SuccessVoidResponseDto getFeed() {
        return null;
    }


    @Operation(summary = "")
    @PatchMapping("/{feedId}")
    public SuccessVoidResponseDto updateFeed() {
        return null;
    }

    @Operation(summary = "")
    @DeleteMapping("/{feedId}")
    public SuccessVoidResponseDto deleteFeed() {
        return null;
    }

    @Operation(summary = "")
    @DeleteMapping("/{feedId}/media")
    public SuccessVoidResponseDto addMedia() {
        return null;
    }

    @Operation(summary = "")
    @PostMapping("/{feedId}/reaction")
    public SuccessVoidResponseDto addReaction() {
        return null;
    }

    @Operation(summary = "")
    @DeleteMapping("/{feedId}/reaction")
    public SuccessVoidResponseDto deleteReaction() {
        return null;
    }
}
