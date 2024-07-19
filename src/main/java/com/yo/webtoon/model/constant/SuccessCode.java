package com.yo.webtoon.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
    SIGNUP("회원가입이 완료되었습니다.", HttpStatus.CREATED),
    WITHDRAWAL("탈퇴가 완료되었습니다.", HttpStatus.OK),
    EDIT("회원 정보 수정이 완료되었습니다.", HttpStatus.OK),
    CERTIFICATION("성인 인증이 완료되었습니다.", HttpStatus.OK),
    CREATE_WEBTOON("웹툰이 생성되었습니다", HttpStatus.CREATED),
    UPDATE_WEBTOON("웹툰 정보가 수정되었습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus httpStatus;
}
