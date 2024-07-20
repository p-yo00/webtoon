package com.yo.webtoon.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class WebtoonAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.toErrorResponse(ErrorCode.UNAUTHORIZED);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
