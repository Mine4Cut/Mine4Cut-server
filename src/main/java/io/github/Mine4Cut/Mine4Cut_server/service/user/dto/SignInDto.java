package io.github.Mine4Cut.Mine4Cut_server.service.user.dto;

import java.time.LocalDateTime;

public record SignInDto(
    String userName,
    String accessToken,
    LocalDateTime loginTime
) {

    public static SignInDto of(String userName, String accessToken) {
        return new SignInDto(userName, accessToken, LocalDateTime.now());
    }

}
