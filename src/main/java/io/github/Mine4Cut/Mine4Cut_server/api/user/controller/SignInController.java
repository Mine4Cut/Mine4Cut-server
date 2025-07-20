package io.github.Mine4Cut.Mine4Cut_server.api.user.controller;

import io.github.Mine4Cut.Mine4Cut_server.api.auth.AuthResponse;
import io.github.Mine4Cut.Mine4Cut_server.api.user.dto.SignInRequest;
import io.github.Mine4Cut.Mine4Cut_server.common.dto.ApiResponse;
import io.github.Mine4Cut.Mine4Cut_server.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SignInController {
    private final UserService userService;

    @PostMapping("/auth/sign-in")
    public AuthResponse signIn(@RequestBody SignInRequest req) throws Exception {
        String accessToken = userService.signIn(req);

        return AuthResponse.of(accessToken);
    }
}
