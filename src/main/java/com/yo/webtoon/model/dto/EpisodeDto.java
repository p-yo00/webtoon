package com.yo.webtoon.model.dto;

import com.yo.webtoon.model.entity.EpisodeContentEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

public class EpisodeDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Upload {

        private Long webtoonId;
        private String title;
        private MultipartFile thumbnailImg;
        private String thumbnailImgUrl;
        private List<MultipartFile> episodeImg;
        private List<EpisodeContentEntity> episodeContents;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime uploadDt;
    }

    @Getter
    @AllArgsConstructor
    public static class ImgUrls {

        List<String> imgUrls;
    }
}
