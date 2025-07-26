package io.github.Mine4Cut.Mine4Cut_server.api.config;

import io.github.Mine4Cut.Mine4Cut_server.domain.user.entity.User;
import io.github.Mine4Cut.Mine4Cut_server.exception.AuthorizationException;

public class UserContextHolder {

    private static final ThreadLocal<User> userContext = new ThreadLocal<>();

    public static User getUser() {
        return userContext.get();
    }

    public static void setUser(User user) {
        userContext.set(user);
    }

    public static Long getUserId() {
        if (userContext.get() == null) {
            throw new AuthorizationException("로그인이 필요합니다.");
        }
        return userContext.get().getId();
    }

    public static void clear() {
        userContext.remove();
    }
}
