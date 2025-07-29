package io.github.Mine4Cut.Mine4Cut_server.config.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "jwt")
@Data
@Component
@Validated
public class JwtProperties {

    @NotBlank
    private String issuer;
    @NotBlank
    private String secret;
    @NotNull
    private Long expiration;
}
