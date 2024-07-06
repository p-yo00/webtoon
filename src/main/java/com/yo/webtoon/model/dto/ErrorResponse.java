package com.yo.webtoon.model.dto;

import com.yo.webtoon.model.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 요청의 실패 응답을 담는 객체
 */
@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {

    private ErrorCode errorCode;
    private HttpStatus httpStatus;
    private String message;

    public static ErrorResponse toErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
            .errorCode(errorCode)
            .message(errorCode.getMessage())
            .httpStatus(errorCode.getHttpStatus())
            .build();
    }
}
