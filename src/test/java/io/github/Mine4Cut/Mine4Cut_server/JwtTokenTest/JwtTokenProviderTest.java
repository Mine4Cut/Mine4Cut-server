package io.github.Mine4Cut.Mine4Cut_server.JwtTokenTest;

import io.github.Mine4Cut.Mine4Cut_server.authentication.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
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
        System.out.println("Test SecretKey: " + secretKey);

        jwtTokenProvider = new JwtTokenProvider(
                userDetailsService,
                ISSUER,
                secretKey,
                EXPIRATION
        );
    }

    @Test
    void createTokenTest() {
        String username = "dorothy";
        String token = jwtTokenProvider.createToken(username);

        System.out.println("Test Token: " + token);

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
    void validateTokenTest() throws Exception {
        String token = jwtTokenProvider.createToken("dorothy");
        jwtTokenProvider.validateToken(token);
    }

    @Test
    void getAuthenticationTest() {
        String username = "dorothy";
        UserDetails user = new User(username, "pwd", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);

        String token = jwtTokenProvider.createToken(username);
        var auth = jwtTokenProvider.getAuthentication(token);

        assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(auth.getName()).isEqualTo(username);
        verify(userDetailsService).loadUserByUsername(username);
    }

    @Test
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
