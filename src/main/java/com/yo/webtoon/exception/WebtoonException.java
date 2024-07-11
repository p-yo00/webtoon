package com.yo.webtoon.exception;

import com.yo.webtoon.model.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class WebtoonException extends RuntimeException {

    private ErrorCode errorCode;
}
