package com.yo.webtoon.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode {
    SIGNUP("회원가입이 완료되었습니다.");

    private final String message;

}
