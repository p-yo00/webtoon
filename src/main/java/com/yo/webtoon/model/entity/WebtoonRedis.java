package com.yo.webtoon.model.entity;

import jakarta.persistence.Id;
import java.util.HashMap;
import java.util.Map;
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
@RedisHash("webtoon_view")
public class WebtoonRedis {

    @Id
    private Long id;
    private Map<Integer, Long> hourlyRecentViews;

    public WebtoonRedis(Long id) {
        this.id = id;
        this.hourlyRecentViews = new HashMap<>();

        for (int hour = 0; hour < 24; hour++) {
            hourlyRecentViews.put(hour, 0L);
        }
    }
}
