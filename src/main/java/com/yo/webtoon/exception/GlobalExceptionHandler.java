package com.yo.webtoon.exception;

import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebtoonException.class)
    public ResponseEntity<ErrorResponse> handleWebtoonException(WebtoonException e) {
        return ResponseEntity
            .status(e.getErrorCode().getHttpStatus())
            .body(ErrorResponse.toErrorResponse(e.getErrorCode()));
    }

    // @Valid 검증에서 실패했을 때 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleNotValidException(
        MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errorCode(ErrorCode.NOT_VALID_INPUT)
                .message(bindingResult.getFieldErrors().get(0).getDefaultMessage())
                .build());
    }

    // 예상치 못 한 RuntimeException 발생 시 예외 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> unexpectedException(RuntimeException e) {
        log.error("Unexpected Exception: ", e);

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.toErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
