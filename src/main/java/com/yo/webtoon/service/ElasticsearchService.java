package com.yo.webtoon.service;

import com.yo.webtoon.model.dto.SuggestRequest;
import com.yo.webtoon.model.dto.SuggestRequest.SearchSuggest;
import com.yo.webtoon.model.dto.SuggestRequest.Suggest;
import com.yo.webtoon.model.dto.SuggestResponse;
import com.yo.webtoon.model.dto.WebtoonIndexDto;
import com.yo.webtoon.model.entity.WebtoonIndex;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final ElasticsearchOperations operations;
    private final RestTemplate restTemplate;

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

    public void deleteWebtoonIndex(Long id) {
        operations.delete(String.valueOf(id), WebtoonIndex.class);
    }

    /**
     * 자동 완성 :: completion 필드에 prefix로 시작하는 값을 리스트로 리턴한다.
     */
    public SuggestResponse.Response autocomplete(String index, String field, String prefix) {
        String url = String.format("/%s/_search", index);

        SuggestRequest.Completion completion = new SuggestRequest.Completion(field);
        SearchSuggest searchSuggest = new SearchSuggest(prefix, completion);
        Suggest suggest = new Suggest(searchSuggest);
        SuggestRequest suggestRequest = new SuggestRequest(suggest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SuggestRequest> request = new HttpEntity<>(suggestRequest, headers);

        SuggestResponse suggestResponse = restTemplate.postForObject(url, request,
            SuggestResponse.class);

        return new SuggestResponse.Response(suggestResponse);
    }

    /**
     * 통합 검색 :: 제목, 작가, 키워드 중 keyword와 일치하는 결과를 리턴한다.
     */
    public List<WebtoonIndexDto> findByKeyword(String keyword) {
        NativeQuery matchQuery = new NativeQueryBuilder()
            .withIds("webtoon_summary")
            .withQuery(q -> q.multiMatch(
                m -> m
                    .fields("title", "author", "keyword")
                    .query(keyword)
            )).build();

        SearchHits<WebtoonIndex> searchHits = operations.search(matchQuery, WebtoonIndex.class);

        return searchHits.getSearchHits().stream().map(x -> new WebtoonIndexDto(x.getContent()))
            .collect(Collectors.toList());
    }
}
