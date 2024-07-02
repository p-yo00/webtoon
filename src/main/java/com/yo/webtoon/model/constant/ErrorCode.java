package com.yo.webtoon.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("일치하는 사용자를 찾을 수 없습니다."),
    ALREADY_EXIST_USER_ID("이미 존재하는 사용자 ID 입니다."),
    FAILED_LOGIN("로그인에 실패했습니다."),
    NOT_VALID_INPUT("입력값 검증에 실패했습니다.");

    private final String message;
}
