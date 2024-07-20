package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.WishlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {

    void deleteByUserIdAndWebtoonId(Long userId, Long webtoonId);
}
