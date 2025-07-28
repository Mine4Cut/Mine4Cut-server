package io.github.Mine4Cut.Mine4Cut_server.service.like.dto;

import java.time.LocalDateTime;

public record LikeDto(
    boolean liked,
    int likeCount,
    LocalDateTime likedTime
) {

    public static LikeDto of(boolean liked, int likeCount) {
        return liked
            ? new LikeDto(true, likeCount, LocalDateTime.now()) :
            new LikeDto(false, likeCount, null);
    }
}
