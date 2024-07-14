package com.yo.webtoon.service;

import com.yo.webtoon.model.dto.WebtoonIndexDto;
import com.yo.webtoon.model.entity.WebtoonIndex;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final ElasticsearchOperations operations;

    public void saveToWebtoonIndex(WebtoonIndexDto webtoonIndexDto) {
        List<String> suggest = new ArrayList<>();
        suggest.add(webtoonIndexDto.getTitle());
        suggest.add(webtoonIndexDto.getAuthor());
        suggest.addAll(webtoonIndexDto.getKeyword());

        operations.save(WebtoonIndex.builder()
            .id(webtoonIndexDto.getId())
            .title(webtoonIndexDto.getTitle())
            .author(webtoonIndexDto.getAuthor())
            .keyword(webtoonIndexDto.getKeyword())
            .thumbnailUrl(webtoonIndexDto.getThumbnailUrl())
            .suggest(new Completion(suggest))
            .build());
    }
}
