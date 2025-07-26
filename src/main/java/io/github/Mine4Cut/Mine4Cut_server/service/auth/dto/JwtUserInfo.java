package io.github.Mine4Cut.Mine4Cut_server.service.auth.dto;

import io.github.Mine4Cut.Mine4Cut_server.domain.user.entity.User;
import java.util.Map;

public record JwtUserInfo(
    String userName,
    String role,
    Map<String, Object> claims
) {

    public static JwtUserInfo of(String userName) {
        return new JwtUserInfo(userName, "ROLE_USER", Map.of());
    }

    public static JwtUserInfo of(String userName, String role) {
        return new JwtUserInfo(userName, role, Map.of());
    }

    public static JwtUserInfo of(User user) {
        return new JwtUserInfo(user.getUsername(), user.getAuthority(), Map.of());
    }
}
