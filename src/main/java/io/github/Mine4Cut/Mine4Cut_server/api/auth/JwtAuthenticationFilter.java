package io.github.Mine4Cut.Mine4Cut_server.api.auth;

import io.github.Mine4Cut.Mine4Cut_server.api.config.UserContextHolder;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.entity.User;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.JwtTokenProvider;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.JwtUserService;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.ParsedJwtInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserService jwtUserSevice;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return "OPTIONS".equals(request.getMethod())
            || ("/users".equals(path) && "POST".equals(request.getMethod()))
            || ("/auth/sign-in".equals(path) && "POST".equals(request.getMethod()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            ParsedJwtInfo parsedJwtInfo = jwtTokenProvider.parseToken(token);
            Authentication authentication = jwtUserSevice.toAuthentication(parsedJwtInfo);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = jwtUserSevice.findUser(parsedJwtInfo);
            UserContextHolder.setUser(user);
        } catch (Exception e) {
            log.error("JWT Authentication Failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            UserContextHolder.clear();
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear();
        }
    }
}
