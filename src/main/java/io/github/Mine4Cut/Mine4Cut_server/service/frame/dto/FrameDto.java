package io.github.Mine4Cut.Mine4Cut_server.service.frame.dto;

import io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity.Frame;

import java.time.LocalDateTime;

public record FrameDto(
    Long frameId,
    String nickname,
    String frameName,
    String imageUrl,
    LocalDateTime createdAt,
    int likeCount,
    int saveCount
) {
    public static FrameDto from(Frame f) {
        return new FrameDto(
            f.getId(),
            f.getNicknameSnapshot(),
            f.getFrameName(),
            f.getImageUrl(),
            f.getCreatedAt(),
            f.getLikeCount(),
            f.getSaveCount()
        );
    }
}
