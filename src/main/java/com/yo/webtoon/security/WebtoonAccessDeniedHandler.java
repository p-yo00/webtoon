package com.yo.webtoon.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class WebtoonAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
            .httpStatus(HttpStatus.FORBIDDEN)
            .errorCode(ErrorCode.FORBIDDEN)
            .message(ErrorCode.FORBIDDEN.getMessage())
            .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
