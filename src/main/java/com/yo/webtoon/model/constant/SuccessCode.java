package com.yo.webtoon.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode {
    SIGNUP("회원가입이 완료되었습니다."),
    WITHDRAWAL("탈퇴가 완료되었습니다."),
    EDIT("회원 정보 수정이 완료되었습니다."),
    CERTIFICATION("성인 인증이 완료되었습니다.");

    private final String message;

}
