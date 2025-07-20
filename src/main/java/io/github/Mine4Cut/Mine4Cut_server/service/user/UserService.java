package io.github.Mine4Cut.Mine4Cut_server.service.user;

import io.github.Mine4Cut.Mine4Cut_server.api.auth.AuthResponse;
import io.github.Mine4Cut.Mine4Cut_server.api.user.dto.SignInRequest;
import io.github.Mine4Cut.Mine4Cut_server.api.user.dto.SingUpRequest;
import io.github.Mine4Cut.Mine4Cut_server.api.user.dto.UserDto;
import io.github.Mine4Cut.Mine4Cut_server.authentication.jwt.JwtTokenProvider;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.User;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService (UserRepository userRepository,
                        AuthenticationManager authenticationManager,
                        JwtTokenProvider jwtTokenProvider,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public UserDto signUp(SingUpRequest req) {
        if (userRepository.existsByUsername(req.username()))
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        if (userRepository.existsByName(req.name()))
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");

        User user = User.builder()
                .username(req.username())
                .password(bCryptPasswordEncoder.encode(req.password()))
                .name(req.name())
                .email(req.email())
                .build();

        userRepository.save(user);

        return new UserDto(user);
    }

    public String signIn(SignInRequest req) throws Exception {
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(req.username(), req.password());
            Authentication auth = authenticationManager.authenticate(token);

            return jwtTokenProvider.createToken(req.username());
        } catch (AuthenticationException ex) {
            throw new Exception("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }
}
