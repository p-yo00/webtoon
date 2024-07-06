package com.yo.webtoon.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("일치하는 사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXIST_USER_ID("이미 존재하는 사용자 ID 입니다.", HttpStatus.BAD_REQUEST),
    FAILED_LOGIN("로그인에 실패했습니다.", HttpStatus.BAD_REQUEST),
    NOT_VALID_INPUT("입력값 검증에 실패했습니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("인증 정보가 없습니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("요청 권한이 없습니다.", HttpStatus.FORBIDDEN),
    WRONG_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("서버 내부 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;
}
