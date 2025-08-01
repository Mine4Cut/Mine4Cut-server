package io.github.Mine4Cut.Mine4Cut_server.api.me.controller;

import io.github.Mine4Cut.Mine4Cut_server.common.dto.PageResponse;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.CustomUserDetails;
import io.github.Mine4Cut.Mine4Cut_server.service.frame.FrameService;
import io.github.Mine4Cut.Mine4Cut_server.service.frame.dto.FrameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class MeController {

    private final FrameService frameService;

    @GetMapping("/frames")
    public PageResponse<FrameDto> getMyFrames(
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return PageResponse.from(frameService.getMyFrames(user.getUserId(),
            PageRequest.of(page, size, Sort.by("createdAt").ascending())));
    }

    @GetMapping("/saved-frames")
    public PageResponse<FrameDto> getSavedFrames(
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return PageResponse.from(frameService.getSavedFrames(user.getUserId(),
            PageRequest.of(page, size, Sort.by("createdAt").ascending())));
    }
}
