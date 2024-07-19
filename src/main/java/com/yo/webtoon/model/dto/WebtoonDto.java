package com.yo.webtoon.model.dto;

import com.yo.webtoon.model.constant.Genre;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

        private Long userId;
        @NotBlank(message = "웹툰 제목을 입력해주세요.")
        private String title;
        private String description;
        private List<String> keyword;
        private MultipartFile img;
        private String imgUrl;
        @Enumerated(value = EnumType.STRING)
        private Genre genre;
        private boolean ageLimit;
        private int uploadCycle;
        private boolean donationAlarm;
    }
}
