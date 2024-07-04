package com.yo.webtoon.security;

import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (tokenProvider.validateToken(token)) {
            Authentication auth = tokenProvider.getAuthentication(token)
                .orElseThrow(
                    () -> new WebtoonException(ErrorCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 어노테이션 권한 인증을 위해 ID를 저장
            request.setAttribute("userId", auth.getName());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * request의 header에서 jwt 토큰을 가져온다.
     */
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length() + 1);
        }
        return null;
    }
}
