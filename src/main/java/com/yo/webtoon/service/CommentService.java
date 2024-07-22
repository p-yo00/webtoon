package com.yo.webtoon.service;

import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.dto.CommentDto.Post;
import com.yo.webtoon.model.entity.CommentEntity;
import com.yo.webtoon.model.entity.CommentRedis;
import com.yo.webtoon.model.entity.EpisodeEntity;
import com.yo.webtoon.model.entity.UserEntity;
import com.yo.webtoon.repository.CommentRedisRepository;
import com.yo.webtoon.repository.CommentRepository;
import com.yo.webtoon.repository.EpisodeRepository;
import com.yo.webtoon.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EpisodeRepository episodeRepository;
    private final CommentRedisRepository commentRedisRepository;
    private final PointService pointService;
    private final UserRepository userRepository;

    /**
     * 에피소드에 댓글을 작성한다.
     */
    public void postComment(Post comment) {
        EpisodeEntity episodeEntity = episodeRepository.findById(comment.getEpisodeId())
            .orElseThrow(() -> new WebtoonException(ErrorCode.EPISODE_NOT_FOUND));

        // 업로드된지 7일이 지났으면 댓글은 DB에 저장된다.
        if (episodeEntity.getUploadDt().isBefore(LocalDateTime.now().minusDays(7))) {
            saveCommentToDb(comment);
        } else { // 7일 이내면 캐시 서버에 저장된다.
            commentRedisRepository.save(CommentRedis.builder()
                .id(comment.getEpisodeId())
                .content(comment.getContent())
                .donationAmount(comment.getDonationAmount())
                .good(0)
                .goodUserList(new ArrayList<>())
                .build());
        }

    }

    private void saveCommentToDb(Post comment) {
        CommentEntity commentEntity = CommentEntity.builder()
            .userId(comment.getUserId())
            .episodeId(comment.getEpisodeId())
            .content(comment.getContent())
            .build();
        commentRepository.save(commentEntity);

        if (comment.getDonationAmount() != null) {
            pointService.donatePoint(commentEntity.getId(),
                comment.getUserId(),
                comment.getDonationAmount());
        }
    }

    /**
     * 댓글에 추천(좋아요)을 한다.
     */
    @Transactional
    public void recommendComment(Long loginId, Long commentId) {
        EpisodeEntity episodeEntity = episodeRepository.findById(commentId)
            .orElseThrow(() -> new WebtoonException(ErrorCode.EPISODE_NOT_FOUND));

        UserEntity userEntity = userRepository.findById(loginId)
            .orElseThrow(() -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        // 업로드된지 7일이 지났으면 DB에 바로 업데이트
        if (episodeEntity.getUploadDt().isBefore(LocalDateTime.now().minusDays(7))) {
            CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new WebtoonException(ErrorCode.COMMENT_NOT_FOUND));

            List<UserEntity> commentUser = commentEntity.getGoods();

            commentUser.add(userEntity);
            commentEntity.setGoods(commentUser);
        } else { // 7일이 지나지 않았으면 캐시서버에 업데이트
            CommentRedis commentRedis = commentRedisRepository.findById(commentId)
                .orElseThrow(() -> new WebtoonException(ErrorCode.COMMENT_NOT_FOUND));

            if (!commentRedis.getGoodUserList().contains(loginId)) {
                commentRedis.setGood(commentRedis.getGood() + 1);
            }
        }
    }
}
