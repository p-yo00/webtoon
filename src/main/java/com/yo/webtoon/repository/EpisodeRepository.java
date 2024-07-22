package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.EpisodeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<EpisodeEntity, Long> {

    Optional<EpisodeEntity> findFirstByWebtoonIdOrderByUploadDtDesc(Long webtoonId);

    Optional<EpisodeEntity> findByWebtoonIdAndEpisodeNum(Long webtoonId, int episodeNum);
}
