package io.github.Mine4Cut.Mine4Cut_server.config.jwt;

import io.github.Mine4Cut.Mine4Cut_server.service.auth.JwtTokenProvider;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(SecretKey secretKey) {
        return new JwtTokenProvider(jwtProperties, secretKey);
    }
}
