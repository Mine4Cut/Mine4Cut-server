package io.github.Mine4Cut.Mine4Cut_server.api.auth.dto;

public record AuthResponse(
    SignInDto signInDto
) {

    public static AuthResponse of(SignInDto accessToken) {
        return new AuthResponse(
            accessToken
        );
    }
}