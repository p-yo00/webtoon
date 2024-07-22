package com.yo.webtoon.model.entity;

import jakarta.persistence.Id;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("comment")
public class CommentRedis {
    @Id
    private Long id;
    private String content;
    private int donationAmount;
    private int good;
    private List<Long> goodUserList;
}
