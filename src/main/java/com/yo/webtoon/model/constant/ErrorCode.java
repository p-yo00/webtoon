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
    INTERNAL_SERVER_ERROR("서버 내부 문제가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    S3_IO_EXCEPTION("S3 이미지 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    WEBTOON_NOT_FOUND("일치하는 웹툰을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    MUST_HAVE_1_EPISODE_TO_OPEN("공개하려면 적어도 하나의 에피소드가 등록되어야 합니다.", HttpStatus.BAD_REQUEST),
    EPISODE_NOT_FOUND("해당하는 에피소드를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    DATA_INTEGRITY_VIOLATION("데이터 무결성이 위반되었습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}
