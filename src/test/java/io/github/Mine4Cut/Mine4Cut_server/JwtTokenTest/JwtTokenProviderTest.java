package io.github.Mine4Cut.Mine4Cut_server.JwtTokenTest;

import io.github.Mine4Cut.Mine4Cut_server.authentication.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtTokenProviderTest {
    @Mock
    private UserDetailsService userDetailsService;

    private JwtTokenProvider jwtTokenProvider;
    private String secretKey;
    private static final String ISSUER = "TestApp";
    private static final long EXPIRATION = 600; // 10분

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        byte[] keyBytes = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded();
        secretKey = Encoders.BASE64URL.encode(keyBytes);

        jwtTokenProvider = new JwtTokenProvider(
                userDetailsService,
                ISSUER,
                secretKey,
                EXPIRATION
        );
    }

    @Test
    @DisplayName("토큰 생성 시점 및 유효 기간에 대해 검증합니다.")
    void createTokenTest() {
        String username = "dorothy";
        String token = jwtTokenProvider.createToken(username);

        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getIssuer()).isEqualTo(ISSUER);
        assertThat(claims.getSubject()).isEqualTo(username);

        Date now = new Date();

        assertThat(claims.getIssuedAt()).isBeforeOrEqualTo(now);
        assertThat(claims.getExpiration()).isAfter(now);

        long diffSeconds = ((claims.getExpiration().getTime() - claims.getIssuedAt().getTime())
                / 1000);
        assertThat(diffSeconds).isCloseTo(EXPIRATION, within(1L));
    }

    @Test
    @DisplayName("Jwt 토큰이 유효하지 않을 시 예외를 발생시킵니다.")
    void validateTokenTest() throws Exception {
        String token = jwtTokenProvider.createToken("dorothy");
        jwtTokenProvider.validateToken(token);
    }

    @Test
    @DisplayName("Jwt 토큰으로 Authentication 토큰을 생성하고 이 토큰에 대해 검증합니다.")
    void generateAuthenticationTest() {
        String username = "dorothy";
        UserDetails user = new User(username, "pwd", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);

        String token = jwtTokenProvider.createToken(username);
        var auth = jwtTokenProvider.generateAuthentication(token);

        assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(auth.getName()).isEqualTo(username);
        verify(userDetailsService).loadUserByUsername(username);
    }

    @Test
    @DisplayName("유효 기간이 지난 토큰을 생성하고 검증 메소드에서 예외를 체크합니다.")
    void expiredTokenTest() {
        JwtTokenProvider expiredProvider = new JwtTokenProvider(
                userDetailsService,
                ISSUER,
                secretKey,
                -10
        );
        String token = expiredProvider.createToken("yong");

        assertThatThrownBy(() -> jwtTokenProvider.validateToken(token))
                .isInstanceOf(Exception.class);
    }
}
