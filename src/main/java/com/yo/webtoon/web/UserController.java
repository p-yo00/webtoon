package com.yo.webtoon.web;

import com.yo.webtoon.model.dto.SuccessResponse;
import com.yo.webtoon.model.dto.UserDto;
import com.yo.webtoon.security.TokenProvider;
import com.yo.webtoon.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    /**
     * 회원가입 :: 아이디, 비밀번호, 이름, 성인인증 여부, 권한을 입력하여 가입한다.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<SuccessResponse> signUp(@RequestBody @Valid UserDto.SignUp request) {
        userService.signUp(request);
        return ResponseEntity.ok(new SuccessResponse("회원가입 완료"));
    }

    /**
     * 로그인 :: JWT 토큰을 쿠키에 저장 및 반환한다.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserDto.Login request) {
        UserDto.Authorization authInfo = userService.authenticate(request);

        String jwtToken = tokenProvider.generateToken(authInfo.getUserId());
        ResponseCookie activeCookie = ResponseCookie.from("activeToken", jwtToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(60 * 60)
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, activeCookie.toString())
            .body(jwtToken);
    }
}
