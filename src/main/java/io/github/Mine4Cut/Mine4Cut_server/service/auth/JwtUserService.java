package io.github.Mine4Cut.Mine4Cut_server.service.auth;

import io.github.Mine4Cut.Mine4Cut_server.domain.user.entity.User;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.repository.UserRepository;
import io.github.Mine4Cut.Mine4Cut_server.exception.NotFoundException;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.JwtUserInfo;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.ParsedJwtInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUserService {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public JwtUserInfo toJwtUserInfo(User user) {
        return JwtUserInfo.of(user);
    }

    public Authentication toAuthentication(ParsedJwtInfo parsedJwtInfo) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(parsedJwtInfo.username());
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }

    public User findUser(ParsedJwtInfo jwtInfo) {
        return userRepository.findByUsername(jwtInfo.username())
            .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));
    }
}
