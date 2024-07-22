package com.yo.webtoon.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CommentDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Post {

        private Long userId;
        private Long episodeId;
        private String content;
        private Integer donationAmount;
    }
}
