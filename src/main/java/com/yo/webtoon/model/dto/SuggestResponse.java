package com.yo.webtoon.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuggestResponse {

    private Suggest suggest;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Suggest {

        @JsonProperty("search-suggest")
        private List<SearchSuggest> searchSuggest;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchSuggest {

        private List<Options> options;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Options {

        private String text;
    }

    @Getter
    public static class Response {

        private final List<String> suggests;

        public Response(SuggestResponse response) {
            suggests = new ArrayList<>();
            for (Options hit : response.suggest.searchSuggest.get(0).options) {
                suggests.add(hit.text);
            }
        }
    }
}
