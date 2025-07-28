package io.github.Mine4Cut.Mine4Cut_server.api.frame.controller;

import io.github.Mine4Cut.Mine4Cut_server.common.dto.ApiResponse;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.CustomUserDetails;
import io.github.Mine4Cut.Mine4Cut_server.service.like.LikeService;
import io.github.Mine4Cut.Mine4Cut_server.service.like.dto.LikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/frames")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{frameId}/like")
    public ApiResponse<LikeDto> toggleLike(
        @PathVariable Long frameId,
        @AuthenticationPrincipal CustomUserDetails user
        ) {
        return ApiResponse.ofSuccess(
            likeService.toggleLike(user.getUserId(), frameId)
        );
    }
}
