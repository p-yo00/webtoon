package com.yo.webtoon.model.entity;

import jakarta.persistence.Id;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.suggest.Completion;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "webtoon_summary")
public class WebtoonIndex {

    @Id
    private Long id;
    private String title;
    private String author;
    @Field(type = FieldType.Keyword)
    private List<String> keyword;
    private String imgUrl;
    @CompletionField
    private Completion suggest;
}
