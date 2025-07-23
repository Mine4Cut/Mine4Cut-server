package io.github.Mine4Cut.Mine4Cut_server.domain.frame.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FrameDto {
    @NotBlank
    private Long userId;

    @NotBlank
    private String frameName;

    @NotBlank
    private String userName;

    @NotBlank
    private int like;
}
