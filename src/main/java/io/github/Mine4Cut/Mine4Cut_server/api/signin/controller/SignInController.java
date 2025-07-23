package io.github.Mine4Cut.Mine4Cut_server.api.signin.controller;

import io.github.Mine4Cut.Mine4Cut_server.api.auth.dto.AuthResponse;
import io.github.Mine4Cut.Mine4Cut_server.api.signin.dto.SignInRequest;
import io.github.Mine4Cut.Mine4Cut_server.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SignInController {
    private final UserService userService;

    @PostMapping("/auth/sign-in")
    public AuthResponse signIn(@RequestBody @Valid SignInRequest req) throws Exception {
        String accessToken = userService.signIn(req);

        return AuthResponse.of(accessToken);
    }
}