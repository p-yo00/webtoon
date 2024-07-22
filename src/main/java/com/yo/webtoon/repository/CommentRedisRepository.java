package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.CommentRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRedisRepository extends CrudRepository<CommentRedis, Long> {

}
