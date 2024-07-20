package com.yo.webtoon.model.dto;

import com.yo.webtoon.model.entity.WebtoonEntity;
import com.yo.webtoon.model.entity.WebtoonIndex;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WebtoonIndexDto {

    private Long id;
    private String title;
    private String author;
    private List<String> keyword;
    private String imgUrl;

    public WebtoonIndexDto(WebtoonIndex index) {
        this.id = index.getId();
        this.title = index.getTitle();
        this.author = index.getAuthor();
        this.keyword = index.getKeyword();
        this.imgUrl = index.getImgUrl();
    }

    public WebtoonIndexDto(WebtoonEntity entity, String authorName) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = authorName;
        this.keyword = entity.getKeyword();
        this.imgUrl = entity.getImgUrl();
    }
}
