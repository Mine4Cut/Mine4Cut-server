package io.github.Mine4Cut.Mine4Cut_server.service.auth;

import io.github.Mine4Cut.Mine4Cut_server.api.user.dto.SignInRequest;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.entity.User;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.repository.UserRepository;
import io.github.Mine4Cut.Mine4Cut_server.exception.NotFoundException;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.JwtUserInfo;
import io.github.Mine4Cut.Mine4Cut_server.api.auth.dto.SignInDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserService jwtUserService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public SignInDto signIn(SignInRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));
        JwtUserInfo jwtUserInfo = jwtUserService.toJwtUserInfo(user);

        return SignInDto.of(user.getUsername(), jwtTokenProvider.createToken(jwtUserInfo));
    }


}
