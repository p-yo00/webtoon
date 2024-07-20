package com.yo.webtoon.repository;

import com.yo.webtoon.model.constant.Genre;
import com.yo.webtoon.model.dto.WebtoonIndexDto;
import com.yo.webtoon.model.entity.WebtoonEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WebtoonRepository extends JpaRepository<WebtoonEntity, Long> {

    @Query("select new com.yo.webtoon.model.dto.WebtoonIndexDto(w, u.userName) "
        + "from WebtoonEntity w join UserEntity u on w.authorId=u.id "
        + "where w.genre=?1 and w.isComplete=?2 and w.isPublic=true "
        + "order by w.recentViewCnt desc")
    List<WebtoonIndexDto> findByGenreAndIsCompleteOrderByRealTimeBest(
        Genre genre, boolean isComplete, Pageable pageable);

    @Query("select new com.yo.webtoon.model.dto.WebtoonIndexDto(w, u.userName) "
        + "from WebtoonEntity w join UserEntity u on w.authorId=u.id "
        + "where w.genre=?1 and w.isComplete=?2 and w.isPublic=true "
        + "order by w.totalViewCnt desc")
    List<WebtoonIndexDto> findByGenreAndIsCompleteOrderByBest(
        Genre genre, boolean isComplete, Pageable pageable);

    @Query("select new com.yo.webtoon.model.dto.WebtoonIndexDto(w, u.userName) "
        + "from WebtoonEntity w join UserEntity u on w.authorId=u.id "
        + "where w.genre=?1 and w.isComplete=?2 and w.isPublic=true "
        + "order by (select e.uploadDt "
        + "from EpisodeEntity e "
        + "where e.webtoonId=w.id "
        + "order by e.uploadDt desc limit 1) desc")
    List<WebtoonIndexDto> findByGenreAndIsCompleteOrderByUpToDate(
        Genre genre, boolean isComplete, Pageable pageable);

    @Query("select new com.yo.webtoon.model.dto.WebtoonIndexDto(w, u.userName) "
        + "from WebtoonEntity w join UserEntity u on w.authorId=u.id "
        + "where w.genre=?1 and w.isComplete=?2 and w.isPublic=true "
        + "order by (select count(*) "
        + "from WishlistEntity "
        + "where webtoonId=w.id) desc")
    List<WebtoonIndexDto> findByGenreAndIsCompleteOrderByWishlist(
        Genre genre, boolean isComplete, Pageable pageable);
}
