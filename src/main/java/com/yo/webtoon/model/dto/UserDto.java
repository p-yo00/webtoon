package com.yo.webtoon.model.dto;

import com.yo.webtoon.model.annotation.ValidRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUp {

        @NotBlank(message = "아이디를 입력해주세요.")
        private String userId;
        @Size(min = 8, max = 16, message = "8자 이상, 16자 이하로 입력해주세요.")
        private String password;
        @NotBlank(message = "이름을 입력해주세요.")
        private String userName;
        private boolean adult;
        @ValidRole
        private String role;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {

        @NotBlank(message = "아이디를 입력해주세요.")
        private String userId;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Edit {

        private String userId;
        @NotBlank(message = "이름을 입력해주세요.")
        private String userName;
        @NotBlank(message = "기존 비밀번호를 입력해주세요.")
        private String oldPassword;
        @NotBlank(message = "새 비밀번호를 입력해주세요.")
        private String newPassword;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Get {

        private String userId;
        private String userName;
        private int point;
        private LocalDateTime registerDatetime;
        private LocalDate adultCertificationDate;
    }
}
