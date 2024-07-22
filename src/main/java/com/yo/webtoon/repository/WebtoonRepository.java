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

    @Query("select w.genre from ViewHistoryEntity vh "
        + "join EpisodeEntity ep on vh.episodeId = ep.id "
        + "join WebtoonEntity w on ep.webtoonId = w.id "
        + "where vh.userId = ?1 "
        + "group by w.genre "
        + "order by count(w.genre) desc")
    List<String> findFavoriteGenre(Long userId, Pageable pageable);

    @Query("select new com.yo.webtoon.model.dto.WebtoonIndexDto(w, u.userName) "
        + "from WebtoonEntity w join UserEntity u on w.authorId=u.id "
        + "where w.isPublic=true and w.id not in ("
        + "select e.webtoonId from ViewHistoryEntity vh "
        + "join EpisodeEntity e on vh.episodeId = e.id "
        + "join WebtoonEntity w on e.webtoonId = w.id "
        + "where vh.userId=?1) "
        + "and w.genre in ?2 "
        + "order by w.totalViewCnt desc")
    List<WebtoonIndexDto> recommendWebtoon(Long userId, List<String> genres, Pageable pageable);
}
