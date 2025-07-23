package io.github.Mine4Cut.Mine4Cut_server.api.signin.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank String username,
        @NotBlank String password
){}
