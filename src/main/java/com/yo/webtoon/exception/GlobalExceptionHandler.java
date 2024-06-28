package com.yo.webtoon.exception;

import com.yo.webtoon.model.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebtoonException.class)
    public ResponseEntity<ErrorResponse> handleWebtoonException(WebtoonException e) {
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(ErrorResponse.builder()
                .httpStatus(e.getHttpStatus())
                .errorCode(e.getErrorCode())
                .message(e.getErrorCode().getMessage())
                .build());
    }
}
