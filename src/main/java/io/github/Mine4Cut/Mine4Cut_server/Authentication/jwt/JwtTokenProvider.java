package io.github.Mine4Cut.Mine4Cut_server.Authentication.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;

    private SecretKey secretKey;
    private long validityInSeconds;

    public JwtTokenProvider(UserDetailsService userDetailsService,
                            @Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration}") long expiration){
        this.userDetailsService = userDetailsService;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));
        this.validityInSeconds = expiration;
    }

    public String createToken(String username) {
        final Date now = new Date();
        final Date expiry = new Date(now.getTime() + validityInSeconds);

        final Claims claims = Jwts.claims()
                .issuer("Mine4Cut")
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey).compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, token , userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT Token: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT Token: {}", e.getMessage());
        }
        return false;
    }
}
