package io.github.Mine4Cut.Mine4Cut_server.JwtTokenTest;

import io.github.Mine4Cut.Mine4Cut_server.config.JwtProperties;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.JwtTokenProvider;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.JwtUserInfo;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.ParsedJwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenProvider 테스트")
class JwtTokenProviderTest {

    private static final String TEST_ISSUER = "test-issuer";
    private static final String TEST_SECRET = "dGVzdC1zZWNyZasdfasdfasXQta2V5LWZvci10ZXN0aW5n";
    private static final Long TEST_EXPIRATION = 1800L; // 30분
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_ROLE = "ROLE_USER";

    private JwtTokenProvider jwtTokenProvider;
    private JwtProperties jwtProperties;
    private SecretKey secretKey = Keys.hmacShaKeyFor(TEST_SECRET.getBytes());

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        jwtProperties = createTestJwtProperties();
        secretKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(jwtProperties.getSecret()));
        jwtTokenProvider = new JwtTokenProvider(jwtProperties, secretKey);
    }

    @Test
    @DisplayName("JwtUserInfo로 JWT 토큰을 생성한다")
    void createToken_WithJwtUserInfo_Success() {
        // given
        JwtUserInfo userInfo = JwtUserInfo.of(TEST_USERNAME);

        // when
        String token = jwtTokenProvider.createToken(userInfo);

        // then
        assertThat(token).isNotNull();
        assertThat(token).contains(".");

        // 토큰 파싱해서 내용 검증
        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        assertThat(claims.getSubject()).isEqualTo(TEST_USERNAME);
        assertThat(claims.getIssuer()).isEqualTo(TEST_ISSUER);
        assertThat(claims.getIssuedAt()).isBeforeOrEqualTo(new Date());
        assertThat(claims.getExpiration()).isAfter(new Date());
        assertThat(claims.get("role")).isEqualTo(TEST_ROLE);
    }

    @Test
    @DisplayName("기본 권한으로 JWT 토큰을 생성한다")
    void createToken_WithDefaultRole_Success() {
        // given
        JwtUserInfo userInfo = JwtUserInfo.of(TEST_USERNAME);

        // when
        String token = jwtTokenProvider.createToken(userInfo);

        // then
        assertThat(token).isNotNull();

        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        assertThat(claims.getSubject()).isEqualTo(TEST_USERNAME);
        assertThat(claims.get("role")).isEqualTo("ROLE_USER"); // 기본 역할 검증
    }

    @Test
    @DisplayName("ADMIN 권한으로 JWT 토큰을 생성한다")
    void createToken_WithAdminRole_Success() {
        // given
        JwtUserInfo userInfo = JwtUserInfo.of(TEST_USERNAME, "ROLE_ADMIN");

        // when
        String token = jwtTokenProvider.createToken(userInfo);

        // then
        assertThat(token).isNotNull();

        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        assertThat(claims.getSubject()).isEqualTo(TEST_USERNAME);
        assertThat(claims.get("role")).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("유효한 JWT 토큰을 파싱한다")
    void parseToken_ValidToken_Success() throws Exception {
        // given
        JwtUserInfo userInfo = JwtUserInfo.of(TEST_USERNAME, TEST_ROLE);
        String token = jwtTokenProvider.createToken(userInfo);

        // when
        ParsedJwtInfo result = jwtTokenProvider.parseToken(token);

        // then
        assertThat(result.username()).isEqualTo(TEST_USERNAME);
        assertThat(result.role()).isEqualTo(TEST_ROLE);
        assertThat(result.issuedAt()).isBeforeOrEqualTo(new Date());
        assertThat(result.expiration()).isAfter(new Date());
    }

    @Test
    @DisplayName("기본 권한 토큰을 파싱한다")
    void parseToken_DefaultRoleToken_Success() throws Exception {
        // given
        JwtUserInfo userInfo = JwtUserInfo.of(TEST_USERNAME); // 기본 권한 사용
        String token = jwtTokenProvider.createToken(userInfo);

        // when
        ParsedJwtInfo result = jwtTokenProvider.parseToken(token);

        // then
        assertThat(result.username()).isEqualTo(TEST_USERNAME);
        assertThat(result.role()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("ADMIN 권한 토큰을 파싱한다")
    void parseToken_AdminRoleToken_Success() throws Exception {
        // given
        JwtUserInfo userInfo = JwtUserInfo.of(TEST_USERNAME, "ROLE_ADMIN");
        String token = jwtTokenProvider.createToken(userInfo);

        // when
        ParsedJwtInfo result = jwtTokenProvider.parseToken(token);

        // then
        assertThat(result.username()).isEqualTo(TEST_USERNAME);
        assertThat(result.role()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("만료된 JWT 토큰 파싱 시 예외를 발생시킨다")
    void parseToken_ExpiredToken_ThrowsException() {
        // given - 만료된 토큰을 위한 설정
        JwtProperties expiredProperties = new JwtProperties();
        expiredProperties.setIssuer(TEST_ISSUER);
        expiredProperties.setSecret(jwtProperties.getSecret());
        expiredProperties.setExpiration(-1L); // 이미 만료된 시간

        JwtTokenProvider expiredProvider = new JwtTokenProvider(expiredProperties, secretKey);
        JwtUserInfo userInfo = JwtUserInfo.of(TEST_USERNAME, TEST_ROLE);
        String expiredToken = expiredProvider.createToken(userInfo);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.parseToken(expiredToken))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("토큰이 만료되었습니다");
    }

    @Test
    @DisplayName("잘못된 형식의 JWT 토큰 파싱 시 예외를 발생시킨다")
    void parseToken_MalformedToken_ThrowsException() {
        // given
        String malformedToken = "invalid.jwt.token";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.parseToken(malformedToken))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("잘못된 형식의 토큰입니다");
    }

    @Test
    @DisplayName("잘못된 서명의 JWT 토큰 파싱 시 예외를 발생시킨다")
    void parseToken_InvalidSignature_ThrowsException() {
        // given - 다른 비밀키로 만든 토큰
        SecretKey wrongKey = Keys.hmacShaKeyFor("wBQsaODDMErasdfasdsaQsadfcadasdongKey".getBytes());
        JwtTokenProvider wrongProvider = new JwtTokenProvider(jwtProperties, wrongKey);
        JwtUserInfo userInfo = JwtUserInfo.of(TEST_USERNAME, TEST_ROLE);
        String wrongToken = wrongProvider.createToken(userInfo);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.parseToken(wrongToken))
            .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("잘못된 발급자의 JWT 토큰 파싱 시 예외를 발생시킨다")
    void parseToken_WrongIssuer_ThrowsException() {
        // given - 다른 발급자로 만든 토큰
        JwtProperties wrongProperties = new JwtProperties();
        wrongProperties.setIssuer("WrongIssuer");
        wrongProperties.setSecret(jwtProperties.getSecret());
        wrongProperties.setExpiration(3600L);

        JwtTokenProvider wrongProvider = new JwtTokenProvider(wrongProperties, secretKey);
        JwtUserInfo userInfo = JwtUserInfo.of(TEST_USERNAME, TEST_ROLE);
        String wrongIssuerToken = wrongProvider.createToken(userInfo);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.parseToken(wrongIssuerToken))
            .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("HTTP 요청에서 Bearer 토큰을 추출한다")
    void resolveToken_ValidBearerToken_Success() throws Exception {
        // given
        String token = "valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // when
        String result = jwtTokenProvider.resolveToken(request);

        // then
        assertThat(result).isEqualTo(token);
    }

    @Test
    @DisplayName("Authorization 헤더가 없을 때 예외를 발생시킨다")
    void resolveToken_NoAuthorizationHeader_ThrowsException() {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.resolveToken(request))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("토큰이 없습니다");
    }

    @Test
    @DisplayName("Bearer 접두사가 없을 때 예외를 발생시킨다")
    void resolveToken_NoBearerPrefix_ThrowsException() {
        // given
        when(request.getHeader("Authorization")).thenReturn("InvalidPrefix token");

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.resolveToken(request))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("토큰이 없습니다");
    }

    @Test
    @DisplayName("빈 Authorization 헤더일 때 예외를 발생시킨다")
    void resolveToken_EmptyAuthorizationHeader_ThrowsException() {
        // given
        when(request.getHeader("Authorization")).thenReturn("");

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.resolveToken(request))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("토큰이 없습니다");
    }

    private JwtProperties createTestJwtProperties() {
        JwtProperties properties = new JwtProperties();
        properties.setIssuer(TEST_ISSUER);
        properties.setSecret(TEST_SECRET);
        properties.setExpiration(TEST_EXPIRATION);
        return properties;
    }

    private JwtUserInfo createTestUserInfo() {
        return JwtUserInfo.of(TEST_USERNAME, TEST_ROLE);
    }

    private JwtUserInfo createTestAdminUserInfo() {
        return JwtUserInfo.of(TEST_USERNAME, "ROLE_ADMIN");
    }
}
