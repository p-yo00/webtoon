package com.yo.webtoon.web;

import com.yo.webtoon.model.dto.SuggestResponse.Response;
import com.yo.webtoon.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/search"))
@RequiredArgsConstructor
public class SearchController {

    private final ElasticsearchService elasticsearchService;

    /**
     * 텍스트 입력 시, 웹툰의 제목/작성자/키워드가 자동완성으로 제공된다.
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<Response> autocomplete(@RequestParam("q") String prefix) {
        return ResponseEntity.ok(
            elasticsearchService.autocomplete("webtoon_summary", "suggest", prefix)
        );
    }

    /**
     * 검색 시, 웹툰의 제목/작성자/키워드와 일치하는 웹툰 검색 결과를 리턴한다.
     */
    @GetMapping
    public ResponseEntity<?> search(@RequestParam("q") String keyword) {
        return ResponseEntity.ok(
            elasticsearchService.findByKeyword(keyword)
        );
    }
}
