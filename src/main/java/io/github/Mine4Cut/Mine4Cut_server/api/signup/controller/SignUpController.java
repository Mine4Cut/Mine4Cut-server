package io.github.Mine4Cut.Mine4Cut_server.api.signup.controller;

import io.github.Mine4Cut.Mine4Cut_server.api.signup.dto.SignUpRequest;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.dto.UserDto;
import io.github.Mine4Cut.Mine4Cut_server.common.dto.ApiResponse;
import io.github.Mine4Cut.Mine4Cut_server.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SignUpController {
    private final UserService userService;

    @PostMapping("/users")
    public ApiResponse<UserDto> signUp(@RequestBody @Valid SignUpRequest req) {
        return ApiResponse.ofSuccess(userService.signUp(req));
    }
}
