package io.github.Mine4Cut.Mine4Cut_server.domain.frame.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FrameDto {
    private Long userId;

    private String nickname;

    private String frameName;

    private String imageUrl;

    private int likeCount;
}