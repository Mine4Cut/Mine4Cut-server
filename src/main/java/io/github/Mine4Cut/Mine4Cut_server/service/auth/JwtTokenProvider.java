package io.github.Mine4Cut.Mine4Cut_server.service.auth;


import io.github.Mine4Cut.Mine4Cut_server.config.jwt.JwtProperties;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.JwtUserInfo;
import io.github.Mine4Cut.Mine4Cut_server.service.auth.dto.ParsedJwtInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public String createToken(JwtUserInfo userInfo) {
        final Date now = new Date();
        final Date expiry = new Date(now.getTime() + jwtProperties.getExpiration() * 1000);

        final Claims claims = Jwts.claims()
            .issuer(jwtProperties.getIssuer())
            .subject(userInfo.userName())
            .issuedAt(now)
            .add("role", userInfo.role())
            .expiration(expiry)
            .build();

        return Jwts.builder()
            .claims(claims)
            .signWith(secretKey).compact();
    }

    public String resolveToken(HttpServletRequest request) throws Exception {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new BadRequestException("토큰이 없습니다.");
    }

    public ParsedJwtInfo parseToken(String token) throws Exception {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(jwtProperties.getIssuer())
                .build()
                .parseSignedClaims(token).getPayload();

            return new ParsedJwtInfo(
                claims.getSubject(),
                claims.get("role", String.class),
                claims.getIssuedAt(),
                claims.getExpiration()
            );
        } catch (ExpiredJwtException e) {
            log.warn("JWT token has expired: {}", e.getMessage());
            throw new BadRequestException("토큰이 만료되었습니다.", e);
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
            throw new BadRequestException("지원하지 않는 토큰 형식입니다.", e);
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
            throw new BadRequestException("잘못된 형식의 토큰입니다.", e);
        } catch (SecurityException e) {
            log.warn("JWT signature validation failed: {}", e.getMessage());
            throw new BadRequestException("토큰 서명 검증에 실패했습니다.", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid: {}", e.getMessage());
            throw new BadRequestException("유효하지 않은 토큰입니다.", e);
        } catch (Exception e) {
            log.error("Unexpected error while parsing JWT token: {}", e.getMessage(), e);
            throw new BadRequestException("토큰 파싱 중 예기치 않은 오류가 발생했습니다.", e);
        }
    }
}
