package io.github.Mine4Cut.Mine4Cut_server.api.frame.controller;

import io.github.Mine4Cut.Mine4Cut_server.common.dto.ApiResponse;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.CustomUserDetails;
import io.github.Mine4Cut.Mine4Cut_server.service.frame.FrameService;
import io.github.Mine4Cut.Mine4Cut_server.service.frame.dto.CreateFrameDto;
import io.github.Mine4Cut.Mine4Cut_server.service.storage.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestController
@RequestMapping("/frames")
@RequiredArgsConstructor
public class FrameController {

    private final FrameService frameService;

    private final StorageService storageService;

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<CreateFrameDto> createFrame(
        HttpServletRequest req,
        @AuthenticationPrincipal CustomUserDetails user,
        @RequestParam("frameName")
        @NotBlank
        @Size // TODO
        String frameName,
        @RequestParam("frame") MultipartFile frame
        ) throws IOException {
        log.info(req.getContentType());

        return ApiResponse.ofSuccess(
            frameService.createFrame(user.getUserId(),
                user.getNickname(),
                frameName,
                storageService.uploadFrame(frame))
        );
    }

    @DeleteMapping("/{frameId}")
    public ApiResponse<?> deleteFrame(
        @PathVariable Long frameId,
        @AuthenticationPrincipal CustomUserDetails user
    ) throws AccessDeniedException {

        storageService.deleteFrameImage(frameService.deleteFrame(user.getUserId(), frameId));

        return ApiResponse.ofDeletion();
    }
}
