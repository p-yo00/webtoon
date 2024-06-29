package com.yo.webtoon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 따로 반환값이 없는 API의 성공 시의 응답을 담는 객체
 */
@AllArgsConstructor
@Getter
public class SuccessResponse {

    String message;
}
