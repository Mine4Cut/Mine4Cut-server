package io.github.Mine4Cut.Mine4Cut_server.api.user.dto;

import io.github.Mine4Cut.Mine4Cut_server.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    // UserDto에 패스워드를 담아서 전달할 이유가 있을까
    /*@NotBlank
    @Size(min = 1, max = 50)
    private String password;*/

    @NotBlank
    @Size(min = 2, max = 10)
    private String name;

    @NotBlank
    private String email;

    public UserDto(User user) {
        this.username = user.getUsername();
        // this.password = user.getPassword();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}