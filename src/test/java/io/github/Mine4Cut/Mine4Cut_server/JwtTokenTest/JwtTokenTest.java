package io.github.Mine4Cut.Mine4Cut_server.JwtTokenTest;


import io.github.Mine4Cut.Mine4Cut_server.Authentication.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;

import java.util.Base64;

public class JwtTokenTest {
    private JwtTokenProvider jwtTokenProvider;

    private final String secretKey = Base64.getEncoder()
            .encodeToString("testKey".getBytes());

    @BeforeEach
    void setUp() {
    }
}
