package io.github.Mine4Cut.Mine4Cut_server.api.frame.controller;

import io.github.Mine4Cut.Mine4Cut_server.common.dto.ApiResponse;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.CustomUserDetails;
import io.github.Mine4Cut.Mine4Cut_server.service.save.SaveService;
import io.github.Mine4Cut.Mine4Cut_server.service.save.dto.SaveDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/frames")
public class SaveController {

    private final SaveService saveService;

    @PostMapping("/{frameId}/saves")
    public ApiResponse<SaveDto> toggleSave(
        @PathVariable Long frameId,
        @AuthenticationPrincipal CustomUserDetails user
        ) throws BadRequestException {
        return ApiResponse.ofSuccess(
            saveService.toggleSave(user.getUserId(), frameId)
        );
    }
}
