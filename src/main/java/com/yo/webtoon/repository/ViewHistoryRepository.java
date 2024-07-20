package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.ViewHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistoryEntity, Long> {

}
