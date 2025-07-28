package io.github.Mine4Cut.Mine4Cut_server.api.frame.controller;

import io.github.Mine4Cut.Mine4Cut_server.api.frame.dto.CreateFrameRequest;
import io.github.Mine4Cut.Mine4Cut_server.common.dto.ApiResponse;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.CustomUserDetails;
import io.github.Mine4Cut.Mine4Cut_server.service.frame.FrameService;
import io.github.Mine4Cut.Mine4Cut_server.service.frame.dto.CreateFrameDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/frames")
public class FrameController {

    private final FrameService frameService;

    @PostMapping
    public ApiResponse<CreateFrameDto> createFrame(
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestBody@Valid CreateFrameRequest req
        ) {
        return ApiResponse.ofSuccess(
            frameService.createFrame(user.getUserId(), user.getNickname(), req)
        );
    }

    @DeleteMapping("/{frameId}/")
    public ApiResponse<?> deleteFrame(
        @PathVariable Long frameId,
        @AuthenticationPrincipal CustomUserDetails user
    ) throws AccessDeniedException {
        frameService.deleteFrame(user.getUserId(), frameId);

        return ApiResponse.ofDeletion();
    }
}
