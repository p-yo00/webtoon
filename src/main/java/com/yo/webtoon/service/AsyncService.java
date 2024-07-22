package com.yo.webtoon.service;

import com.yo.webtoon.model.entity.WebtoonEntity;
import com.yo.webtoon.model.entity.WebtoonRedis;
import com.yo.webtoon.repository.WebtoonRedisRepository;
import com.yo.webtoon.repository.WebtoonRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonRedisRepository webtoonRedisRepository;

    @Async
    @Transactional
    public void asyncUpdateWebtoonView(WebtoonRedis webtoonRedis, int curHour) {
        Map<Integer, Long> hourlyRecentViewMap = webtoonRedis.getHourlyRecentViews();
        Long dailyView = 0L;
        int before1Hour = (curHour - 1) < 0 ? 23 : curHour - 1;

        for (Long view : hourlyRecentViewMap.values()) {
            dailyView += view;
        }

        Optional<WebtoonEntity> webtoonEntity = webtoonRepository.findById(webtoonRedis.getId());

        if (webtoonEntity.isPresent()) {
            webtoonEntity.get().setRecentViewCnt(dailyView);
            webtoonEntity.get().setTotalViewCnt(
                webtoonEntity.get().getTotalViewCnt() + hourlyRecentViewMap.get(before1Hour));
        } else { // 실제 테이블에 없지만 redis에 있다면 redis에서 삭제해준다.
            webtoonRedisRepository.deleteById(webtoonRedis.getId());
        }

        // 현재 시간 조회수는 새로 집계하기 위해 0으로 설정
        hourlyRecentViewMap.put(curHour, 0L);
        webtoonRedis.setHourlyRecentViews(hourlyRecentViewMap);
        webtoonRedisRepository.save(webtoonRedis);
    }

}
