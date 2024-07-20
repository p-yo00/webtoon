package com.yo.webtoon.service;

import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.constant.SseCode;
import com.yo.webtoon.model.dto.EpisodeDto.Upload;
import com.yo.webtoon.model.entity.EpisodeContentEntity;
import com.yo.webtoon.model.entity.EpisodeEntity;
import com.yo.webtoon.model.entity.WebtoonEntity;
import com.yo.webtoon.repository.EpisodeRepository;
import com.yo.webtoon.repository.WebtoonRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final WebtoonRepository webtoonRepository;
    private final EpisodeRepository episodeRepository;
    private final AmazonS3Service amazonS3Service;
    private final SseService sseService;

    /**
     * 에피소드 업로드 : episode 테이블과 S3에 저장하고 업로드 알림 전송
     */
    @Transactional
    public void uploadEpisode(Upload episode, Long loginId) {
        String episodeFormat = "webtoon_%d_episode_%d";
        String contentFormat = "webtoon_%d_episode_%d_%d";

        WebtoonEntity webtoonEntity = webtoonRepository.findById(episode.getWebtoonId())
            .orElseThrow(() -> new WebtoonException(ErrorCode.WEBTOON_NOT_FOUND));

        if (!webtoonEntity.getAuthorId().equals(loginId)) {
            throw new WebtoonException(ErrorCode.FORBIDDEN);
        }

        // 이전 업로드일과 이전 회차를 참조하기위해 웹툰의 가장 최근 업로드된 에피소드를 조회한다.
        EpisodeEntity lastEpisode = episodeRepository.findFirstByWebtoonIdOrderByUploadDtDesc(
            webtoonEntity.getId()).orElse(EpisodeEntity.builder().episodeNum(0).build());

        // 에피소드가 하나도 없는 상태에선 직접 업로드 날짜 입력이 필수
        if (episode.getUploadDt() == null && lastEpisode.getEpisodeNum() == 0) {
            throw new WebtoonException(ErrorCode.NOT_VALID_INPUT);
        }
        // 에피소드가 있고 업로드 날짜 입력이 없다면 다음 업로드 예정일을 자동 계산
        if (episode.getUploadDt() == null) {
            episode.setUploadDt(
                computeUploadDt(lastEpisode.getUploadDt(), webtoonEntity.getUploadCycle()));
        }

        String thumbnailImgKey = String.format(episodeFormat,
            webtoonEntity.getId(), lastEpisode.getEpisodeNum() + 1);

        amazonS3Service.putObject(thumbnailImgKey, episode.getThumbnailImg());

        // 에피소드 정보 db 저장
        EpisodeEntity newEpisode = EpisodeEntity.toEntity(episode, lastEpisode.getEpisodeNum() + 1);

        List<EpisodeContentEntity> episodeContentEntities = new ArrayList<>();
        int sequenceNum = 1;
        for (MultipartFile file : episode.getEpisodeImg()) {
            String imgKey = String.format(contentFormat,
                webtoonEntity.getId(), lastEpisode.getEpisodeNum() + 1, sequenceNum);

            amazonS3Service.putObject(imgKey, file);

            episodeContentEntities.add(EpisodeContentEntity.builder()
                .episode(newEpisode)
                .imgUrl(amazonS3Service.getImgUrl(imgKey))
                .sequenceNum(sequenceNum++)
                .build());
        }

        newEpisode.setThumbnailUrl(amazonS3Service.getImgUrl(thumbnailImgKey));
        newEpisode.setEpisodeContents(episodeContentEntities);
        episodeRepository.save(newEpisode);

        // 업로드 알림 전송
        sseService.sendAlarm(SseCode.UPLOAD_EPISODE, webtoonEntity.getTitle() + " 가 업로드 되었습니다.");
    }

    /**
     * 마지막 업로드 날짜에서 업로드 주기에 맞게 다음 업로드 날짜를 계산
     */
    private LocalDateTime computeUploadDt(LocalDateTime lastUploadDt, int uploadCycle) {
        LocalDateTime uploadDt = lastUploadDt.plusDays(uploadCycle);

        // 업로드 해야 될 날짜가 지났다면 당일 (10분 뒤) 바로 업로드
        if (uploadDt.isAfter(LocalDateTime.now())) {
            uploadDt = LocalDateTime.now().plusMinutes(10);
        }

        return uploadDt;
    }
}
