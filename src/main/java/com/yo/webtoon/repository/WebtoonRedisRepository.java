package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.WebtoonRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebtoonRedisRepository extends CrudRepository<WebtoonRedis, Long> {

}
