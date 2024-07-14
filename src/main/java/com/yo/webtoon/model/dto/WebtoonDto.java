package com.yo.webtoon.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class WebtoonDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {

        private Long authorId;
        @NotBlank(message = "웹툰 제목을 입력해주세요.")
        private String title;
        private String description;
        private List<String> keyword;
        private MultipartFile img;
        private String imgUrl;
        @NotBlank(message = "장르를 선택해주세요.")
        private String genre;
        @NotBlank(message = "연령제한을 선택해주세요.")
        private boolean ageLimit;
        @NotBlank(message = "업로드 주기를 선택해주세요.")
        private int uploadCycle;
        @NotBlank(message = "알람을 설정해주세요.")
        boolean donationAlarm;
    }
}
