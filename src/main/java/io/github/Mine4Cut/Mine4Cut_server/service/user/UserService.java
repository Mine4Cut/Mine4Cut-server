package io.github.Mine4Cut.Mine4Cut_server.service.user;

import io.github.Mine4Cut.Mine4Cut_server.api.user.dto.SignUpRequest;
import io.github.Mine4Cut.Mine4Cut_server.service.user.dto.UserDto;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.entity.User;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserDto signUp(SignUpRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByNickname(req.nickname())) {
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        }

        User user = User.builder()
            .username(req.username())
            .password(bCryptPasswordEncoder.encode(req.password()))
            .nickname(req.nickname())
            .email(req.email())
            .build();

        userRepository.save(user);

        return new UserDto(user);
    }

}