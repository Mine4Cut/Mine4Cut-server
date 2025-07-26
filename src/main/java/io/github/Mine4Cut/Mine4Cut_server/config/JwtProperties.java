package io.github.Mine4Cut.Mine4Cut_server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Data
@Component
public class JwtProperties {

    private String issuer;
    private String secret;
    private Long expiration;
}
