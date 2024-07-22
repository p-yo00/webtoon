package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistoryEntity, Long> {

}
