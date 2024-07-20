package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.ViewHistoryEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistoryEntity, Long> {

    Optional<ViewHistoryEntity> findByEpisodeIdAndUserId(Long episodeId, Long userId);
}
