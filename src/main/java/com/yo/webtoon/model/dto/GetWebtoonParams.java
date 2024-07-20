package com.yo.webtoon.model.dto;

import com.yo.webtoon.model.constant.Genre;
import com.yo.webtoon.model.constant.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetWebtoonParams {

    private Order order;
    private boolean complete;
    private Genre genre;
}
