package com.yo.webtoon.security;

import com.yo.webtoon.model.dto.UserDetail;
import com.yo.webtoon.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final SecretKey secretKey;
    private final UserService userService;

    /**
     * 아이디를 이용해 JWT 토큰을 생성한다.
     */
    public String generateToken(String userId) {
        int duration = 1000 * 60 * 60;
        return Jwts.builder()
            .subject(userId)
            .issuedAt(new Date())
            .expiration(new Date(new Date().getTime() + duration))
            .signWith(secretKey)
            .compact();
    }

    /**
     * 토큰에서 Authentication 객체를 리턴한다.
     */
    public Optional<Authentication> getAuthentication(String token) {
        UserDetail user = userService.loadUserByUsername(parseClaims(token).getSubject());
        return user.isEnabled() ?
            Optional.of(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()))
            : Optional.empty();
    }

    /**
     * 토큰이 유효한지 검증한다.
     */
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    /**
     * 토큰을 파싱해서 Claims 객체를 얻는다.
     */
    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload();
    }
}
