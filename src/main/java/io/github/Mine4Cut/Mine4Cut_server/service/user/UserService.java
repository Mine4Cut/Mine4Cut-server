package io.github.Mine4Cut.Mine4Cut_server.service.user;

import io.github.Mine4Cut.Mine4Cut_server.api.auth.AuthResponse;
import io.github.Mine4Cut.Mine4Cut_server.api.user.LogInRequest;
import io.github.Mine4Cut.Mine4Cut_server.api.user.SingUpRequest;
import io.github.Mine4Cut.Mine4Cut_server.authentication.jwt.JwtTokenProvider;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.User;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService (UserRepository userRepository,
                        AuthenticationManager authenticationManager,
                        JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public void signUp(SingUpRequest req) {
        if (userRepository.existsByUsername(req.username()))
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        if (userRepository.existsByName(req.name()))
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");

        User user = User.builder()
                .username(req.username())
                .password(req.password())
                .name(req.name())
                .build();

        userRepository.save(user);
    }

    public AuthResponse logIn(LogInRequest req) throws Exception {
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(req.username(), req.password());
            Authentication auth = authenticationManager.authenticate(token);

            String jwt = jwtTokenProvider.createToken(auth.getName());
            return new AuthResponse(jwt);
        } catch (AuthenticationException ex) {
            throw new Exception("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }
}
