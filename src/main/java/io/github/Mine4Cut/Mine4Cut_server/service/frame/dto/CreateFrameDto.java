package io.github.Mine4Cut.Mine4Cut_server.service.frame.dto;

import java.time.LocalDateTime;

public record CreateFrameDto(
    Long userId,
    String nickname,
    String frameName,
    String imageUrl,
    LocalDateTime createAt
) {

    public static CreateFrameDto of(Long userId, String nickname, String frameName,
                                    String imageUrl) {
        return new CreateFrameDto(userId, nickname, frameName, imageUrl, LocalDateTime.now());
    }
}