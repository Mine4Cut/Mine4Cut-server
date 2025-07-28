package io.github.Mine4Cut.Mine4Cut_server.api.frame.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CreateFrameRequest(
    // TODO 사이즈 논의
    @NotBlank String frameName,
    @NotBlank @URL(message = "유효한 URL 형식이 아닙니다.") String imageUrl
) {

}