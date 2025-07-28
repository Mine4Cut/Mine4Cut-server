package io.github.Mine4Cut.Mine4Cut_server.service.user.dto;

import io.github.Mine4Cut.Mine4Cut_server.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;

    private String nickname;

    private String email;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }
}