package io.github.Mine4Cut.Mine4Cut_server.authentication.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;

    private final String issuer;
    private final SecretKey secretKey;
    private final Long validityInSeconds;

    public JwtTokenProvider(UserDetailsService userDetailsService,
                            @Value("${spring.application.name}") String name,
                            @Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration}") long expiration){
        this.userDetailsService = userDetailsService;
        this.issuer = name;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));
        this.validityInSeconds = expiration;
    }

    public String createToken(String username) {
        final Date now = new Date();
        final Date expiry = new Date(now.getTime() + validityInSeconds * 1000);

        final Claims claims = Jwts.claims()
                .issuer(issuer)
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey).compact();
    }

    public Authentication createAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                token,
                userDetails.getAuthorities()
        );
    }

    private String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public String resolveToken(HttpServletRequest request) throws Exception {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new Exception();
    }

    public void validateToken(String token) throws Exception {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT Token: {}", e.getMessage());
            throw new Exception();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT Token: {}", e.getMessage());
            throw new Exception();
        }
    }
}
