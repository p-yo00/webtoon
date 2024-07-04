package com.yo.webtoon.web;

import com.yo.webtoon.model.constant.SuccessCode;
import com.yo.webtoon.model.dto.LoginUser;
import com.yo.webtoon.model.dto.SuccessResponse;
import com.yo.webtoon.model.dto.UserDto;
import com.yo.webtoon.security.TokenProvider;
import com.yo.webtoon.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        HttpStatus statusCode = HttpStatus.CREATED;

        return ResponseEntity
            .status(statusCode)
            .body(SuccessResponse.builder()
                .successCode(SuccessCode.SIGNUP)
                .message(SuccessCode.SIGNUP.getMessage())
                .httpStatus(statusCode)
                .build());
    }

    /**
     * 로그인 :: JWT 토큰을 쿠키에 저장 및 반환한다.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserDto.Login request) {
        String loginId = userService.authenticate(request);

        String jwtToken = tokenProvider.generateToken(loginId);
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

    /**
     * 회원 삭제 :: 회원의 탈퇴일시 값을 현재 값으로 업데이트한다.
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{deleteId}")
    public ResponseEntity<SuccessResponse> deleteUser(@LoginUser String loginId,
        @PathVariable("deleteId") String deleteId) {
        userService.deleteUser(loginId, deleteId);

        return ResponseEntity.ok(SuccessResponse.builder()
            .httpStatus(HttpStatus.OK)
            .successCode(SuccessCode.WITHDRAWAL)
            .message(SuccessCode.WITHDRAWAL.getMessage())
            .build());
    }
}
