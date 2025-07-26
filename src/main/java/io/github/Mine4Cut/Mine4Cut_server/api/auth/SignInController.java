package io.github.Mine4Cut.Mine4Cut_server.api.auth;

import io.github.Mine4Cut.Mine4Cut_server.api.auth.dto.AuthResponse;
import io.github.Mine4Cut.Mine4Cut_server.api.user.dto.SignInRequest;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.AuthService;
import io.github.Mine4Cut.Mine4Cut_server.service.user.dto.SignInDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SignInController {

    private final AuthService authService;

    @PostMapping("/auth/sign-in")
    public AuthResponse signIn(@RequestBody @Valid SignInRequest req) {
        SignInDto signInDto = authService.signIn(req);
        return AuthResponse.of(signInDto);
    }
}