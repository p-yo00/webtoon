package com.yo.webtoon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;


@AllArgsConstructor
@Getter
public class AuthenticationAndId {

    private Authentication authentication;
    private Long userId;
}
