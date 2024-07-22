package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.EpisodeContentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeContentRepository extends JpaRepository<EpisodeContentEntity, Long> {
    List<EpisodeContentEntity> findByEpisodeIdOrderBySequenceNum(Long episodeId);
}
