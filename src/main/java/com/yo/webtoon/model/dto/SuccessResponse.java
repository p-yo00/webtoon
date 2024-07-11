package com.yo.webtoon.model.dto;

import com.yo.webtoon.model.constant.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 요청의 성공 응답을 담는 객체
 */
@AllArgsConstructor
@Builder
@Getter
public class SuccessResponse {

    private SuccessCode successCode;
    private HttpStatus httpStatus;
    private String message;

    public static SuccessResponse toSuccessResponse(SuccessCode successCode) {
        return SuccessResponse.builder()
            .successCode(successCode)
            .httpStatus(successCode.getHttpStatus())
            .message(successCode.getMessage())
            .build();
    }
}
