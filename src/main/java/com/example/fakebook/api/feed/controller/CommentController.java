package com.example.fakebook.api.feed.controller;


import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment Controller", description = "Comment Controller")
@RestController
@RequestMapping("/feed/{feedId}/comments")
@RequiredArgsConstructor
public class CommentController {
    @Operation(summary = "")
    @GetMapping
    public SuccessVoidResponseDto getComments(@PathVariable Long feedId) {
        return null;
    }

    @Operation(summary = "")
    @PostMapping
    public SuccessVoidResponseDto createComment(@PathVariable Long feedId) {
        return null;
    }

    @Operation(summary = "")
    @PatchMapping
    public SuccessVoidResponseDto updateComment(@PathVariable Long feedId) {
        return null;
    }

    @Operation(summary = "")
    @DeleteMapping
    public SuccessVoidResponseDto deleteComment(@PathVariable Long feedId) {
        return null;
    }

    @Operation(summary = "")
    @GetMapping("/{commentId}")
    public SuccessVoidResponseDto getComment(@PathVariable Long feedId) {
        return null;
    }

    @Operation(summary = "")
    @PostMapping("/{commentId}/media")
    public SuccessVoidResponseDto addMedia(@PathVariable Long feedId) {
        return null;
    }

    @Operation(summary = "")
    @PostMapping("/{commentId}/reaction")
    public SuccessVoidResponseDto addReaction(@PathVariable Long feedId) {
        return null;
    }

    @Operation(summary = "")
    @DeleteMapping("/{commentId}/reaction")
    public SuccessVoidResponseDto deleteReaction(@PathVariable Long feedId) {
        return null;
    }
}
