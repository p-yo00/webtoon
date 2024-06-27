package com.yo.webtoon.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDto {

    @Getter
    @Setter
    public static class SignUp {

        @NotBlank(message = "아이디를 입력해주세요.")
        private String userId;
        @Size(min = 8, max = 16, message = "8자 이상, 16자 이하로 입력해주세요.")
        private String password;
        @NotBlank(message = "이름을 입력해주세요.")
        private String userName;
        private boolean isAdult;
        private String role;
    }

    @Getter
    @Setter
    public static class Login {

        @NotBlank(message = "아이디를 입력해주세요.")
        private String userId;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Authorization {

        private String userId;
        private String role;
    }
}
