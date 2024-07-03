package com.yo.webtoon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.constant.Role;
import com.yo.webtoon.model.dto.UserDto;
import com.yo.webtoon.model.dto.UserDto.SignUp;
import com.yo.webtoon.model.entity.UserEntity;
import com.yo.webtoon.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("회원가입 - 중복된 ID를 입력하는 경우")
    void signUp_ALREADY_EXIST_USER() {
        // given
        UserDto.SignUp request = SignUp.builder()
            .userId("test").password("1234").adult(false).userName("test").role("ROLE_GENERAL")
            .build();
        given(userRepository.existsByUserId("test"))
            .willReturn(true);

        // when & then
        WebtoonException e = assertThrows(WebtoonException.class,
            () -> userService.signUp(request));

        assertEquals(ErrorCode.ALREADY_EXIST_USER_ID, e.getErrorCode());
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void signUp_SUCCESS() {
        // given
        UserDto.SignUp request = SignUp.builder()
            .userId("test").password("1234").adult(false).userName("test")
            .build();
        given(userRepository.existsByUserId("test"))
            .willReturn(false);
        given(passwordEncoder.encode("1234")).willReturn("4321");

        // then
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        userService.signUp(request);
        verify(userRepository, times(1)).save(captor.capture());

        // when
        Assertions.assertEquals(captor.getValue().getUserId(), "test");
        Assertions.assertEquals(captor.getValue().getPassword(), "4321");
        Assertions.assertEquals(captor.getValue().getPoint(), 0);
        Assertions.assertNull(captor.getValue().getAdultCertificationDate());
        Assertions.assertEquals(captor.getValue().getRole(), Role.ROLE_GENERAL);
    }

    @Test
    @DisplayName("로그인 - 존재하지 않는 아이디 입력")
    void login_NO_EXIST_ID() {
        // given
        UserDto.Login userDto = new UserDto.Login("id", "pw");
        given(userRepository.findByUserId("id")).willReturn(Optional.empty());

        // then & when
        WebtoonException e = assertThrows(WebtoonException.class,
            () -> userService.authenticate(userDto));

        assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
        assertEquals(ErrorCode.FAILED_LOGIN, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인 - 틀린 비밀번호 입력")
    void login_NO_EXIST_PASSWORD() {
        // given
        UserDto.Login userDto = new UserDto.Login("id", "wrong-pw");
        given(userRepository.findByUserId("id")).willReturn(
            Optional.of(UserEntity.builder()
                .userId("id").password("pw").build()));
        given(passwordEncoder.matches("wrong-pw", "pw")).willReturn(false);

        // then & when
        WebtoonException e = assertThrows(WebtoonException.class,
            () -> userService.authenticate(userDto));

        assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
        assertEquals(ErrorCode.FAILED_LOGIN, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인 - 성공")
    void login_SUCCESS() {
        // given
        UserDto.Login userDto = new UserDto.Login("id", "pw");
        given(userRepository.findByUserId("id")).willReturn(
            Optional.of(UserEntity.builder()
                .userId("id").password("encode-pw").role(Role.ROLE_GENERAL)
                .build()));
        given(passwordEncoder.matches("pw", "encode-pw")).willReturn(true);

        // then & when
        String loginId = userService.authenticate(userDto);

        assertEquals("id", loginId);
    }
}