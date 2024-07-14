package com.yo.webtoon.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@Getter
@Setter
public class SuggestRequest {
    private Suggest suggest;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Suggest {
        @JsonProperty("search-suggest")
        private SearchSuggest searchSuggest;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class SearchSuggest {
        private String prefix;
        private Completion completion;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Completion {
        private String field;
    }
}
