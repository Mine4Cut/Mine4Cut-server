package io.github.Mine4Cut.Mine4Cut_server.api.user;

import jakarta.validation.constraints.NotBlank;

public record LogInRequest (
        @NotBlank String username,
        @NotBlank String password
){}
