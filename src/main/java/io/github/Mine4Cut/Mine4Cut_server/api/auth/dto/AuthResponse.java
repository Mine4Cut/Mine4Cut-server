package io.github.Mine4Cut.Mine4Cut_server.api.auth.dto;

public record AuthResponse(
        String accessToken
) {
    public static AuthResponse of(String accessToken) {
        return new AuthResponse(
                accessToken
        );
    }
}