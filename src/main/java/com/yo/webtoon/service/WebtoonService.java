package com.yo.webtoon.service;

import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.dto.GetWebtoonParams;
import com.yo.webtoon.model.dto.WebtoonDto.Create;
import com.yo.webtoon.model.dto.WebtoonDto.EditConfig;
import com.yo.webtoon.model.dto.WebtoonDto.EditInfo;
import com.yo.webtoon.model.dto.WebtoonIndexDto;
import com.yo.webtoon.model.entity.UserEntity;
import com.yo.webtoon.model.entity.WebtoonEntity;
import com.yo.webtoon.model.entity.WebtoonRedis;
import com.yo.webtoon.repository.UserRepository;
import com.yo.webtoon.repository.WebtoonRedisRepository;
import com.yo.webtoon.repository.WebtoonRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final AmazonS3Service amazonS3Service;
    private final ElasticsearchService elasticsearchService;
    private final UserRepository userRepository;
    private final WebtoonRedisRepository webtoonRedisRepository;
    private final AsyncService asyncService;

    /**
     * 새로운 웹툰 정보를 mysql DB, elasticsearch에 저장하고, s3에 웹툰 대표이미지를 저장한다.
     */
    @Transactional
    public void createWebtoon(Create newWebtoon) {
        WebtoonEntity webtoonEntity = WebtoonEntity.toEntity(newWebtoon);
        webtoonRepository.save(webtoonEntity);

        String imgKey = "webtoon_" + webtoonEntity.getId();
        String imgUrl = amazonS3Service.getImgUrl(imgKey);
        webtoonEntity.setImgUrl(imgUrl);

        amazonS3Service.putObject(imgKey, newWebtoon.getImg());
    }

    /**
     * 작가가 웹툰의 정보를 수정한다. elasticsearch에 인덱싱된 웹툰 정보와 s3에 웹툰 대표 이미지를 수정한다.
     */
    @Transactional
    public void updateWebtoonInfo(EditInfo updateDto) {
        WebtoonEntity webtoonEntity = webtoonRepository.findById(updateDto.getWebtoonId())
            .orElseThrow(() -> new WebtoonException(ErrorCode.WEBTOON_NOT_FOUND));

        UserEntity loginUser = userRepository.findById(updateDto.getUserId()).orElseThrow(
            () -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        if (!webtoonEntity.getAuthorId().equals(loginUser.getId())) {
            throw new WebtoonException(ErrorCode.FORBIDDEN);
        }

        webtoonEntity.setDescription(updateDto.getDescription());
        webtoonEntity.setKeyword(updateDto.getKeyword());
        webtoonEntity.setUploadCycle(updateDto.getUploadCycle());
        webtoonEntity.setDonationAlarm(updateDto.isDonationAlarm());

        // 도큐먼트 수정은 삭제 후 다시 생성해야하므로 키워드가 기존 값에서 바뀐 경우에만 한다.
        if (webtoonEntity.isPublic() && !updateDto.getKeyword().equals(webtoonEntity.getKeyword())) {
            elasticsearchService.saveToWebtoonIndex(
                new WebtoonIndexDto(webtoonEntity, loginUser.getUserName()));
        }

        // 처음 웹툰을 등록했을 때 저장했던 공간에 덮어쓴다.
        String imgKey = "webtoon_" + webtoonEntity.getId();
        amazonS3Service.putObject(imgKey, updateDto.getImg());
    }


    /**
     * 관리자가 웹툰의 설정을 수정한다.
     */
    @Transactional
    public void updateWebtoonConfig(EditConfig updateDto) {
        WebtoonEntity webtoonEntity = webtoonRepository.findById(updateDto.getWebtoonId())
            .orElseThrow(() -> new WebtoonException(ErrorCode.WEBTOON_NOT_FOUND));

        UserEntity loginUser = userRepository.findById(webtoonEntity.getAuthorId()).orElseThrow(
            () -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        webtoonEntity.setTitle(updateDto.getTitle());
        webtoonEntity.setGenre(updateDto.getGenre());
        webtoonEntity.setHasAgeLimit(updateDto.isAgeLimit());

        // 도큐먼트 수정은 삭제 후 다시 생성해야하므로 제목이 기존 값에서 바뀐 경우에만 한다.
        if (webtoonEntity.isPublic() && updateDto.getTitle().equals(webtoonEntity.getTitle())) {
            elasticsearchService.saveToWebtoonIndex(
                new WebtoonIndexDto(webtoonEntity, loginUser.getUserId()));
        }
    }

    /**
     * 웹툰을 완결 상태로 변경한다.
     */
    @Transactional
    public void completeWebtoon(Long loginId, Long webtoonId) {
        WebtoonEntity webtoonEntity = webtoonRepository.findById(webtoonId)
            .orElseThrow(() -> new WebtoonException(ErrorCode.WEBTOON_NOT_FOUND));

        UserEntity loginUser = userRepository.findById(loginId).orElseThrow(
            () -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        if (!webtoonEntity.getAuthorId().equals(loginUser.getId())) {
            throw new WebtoonException(ErrorCode.FORBIDDEN);
        }

        webtoonEntity.setComplete(true);
    }

    /**
     * 관리자가 웹툰을 공개 또는 비공개 상태로 변경한다. (isPublic field을 true or false로)
     */
    @Transactional
    public void openOrCloseWebtoon(Long webtoonId) {
        WebtoonEntity webtoonEntity = webtoonRepository.findById(webtoonId)
            .orElseThrow(() -> new WebtoonException(ErrorCode.WEBTOON_NOT_FOUND));

        UserEntity authorUser = userRepository.findById(webtoonEntity.getAuthorId()).orElseThrow(
            () -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        webtoonEntity.setPublic(!webtoonEntity.isPublic());

        // 공개 상태로 변경되었다면 elasticsearch에 웹툰 검색 정보를 저장하고, redis에 웹툰의 시간마다 조회수를 저장하는 key 생성
        if (webtoonEntity.isPublic()) {
            elasticsearchService.saveToWebtoonIndex(new WebtoonIndexDto(webtoonEntity,
                authorUser.getUserName()));
            webtoonRedisRepository.save(new WebtoonRedis(webtoonId));
        } else { // 비공개 상태로 변경되었다면 삭제
            elasticsearchService.deleteWebtoonIndex(webtoonEntity.getId());
            webtoonRedisRepository.deleteById(webtoonId);
        }

        log.info(String.format("[%s: %s] 웹툰의 공개 상태가 %b로 변경되었습니다.",
            webtoonEntity.getId(), webtoonEntity.getTitle(), webtoonEntity.isPublic()));
    }

    /**
     * 웹툰을 [완결여부]와 [장르별]로 '분류'하고 [인기순], [실시간인기순], [최신업로드순], [찜많은순]으로 '정렬'하여 조회할 수 있다.
     */
    public List<WebtoonIndexDto> getWebtoon(GetWebtoonParams webtoonParams, Pageable pageable) {
        return webtoonParams.getOrder().search(
            webtoonRepository, webtoonParams.getGenre(), webtoonParams.isComplete(), pageable
        );
    }

    /*
        매시간 redis에 저장된 모든 웹툰의 조회수로 DB의 전체 조회수, 실시간 조회수 항목에 업데이트한다.
        [전체 조회수]는 이전 한시간동안 기록된 조회수를 기존값에 더해준다.
        [실시간 조회수]는 0~23시에 기록돼있는 조회수를 모두 더한다.
        현재 시간에 저장된 조회수는 0으로 리셋한다.
    */
    @Scheduled(cron = "0 0 0/1 * * *")
    @Transactional
    public void updateWebtoonView() {
        int curHour = LocalDateTime.now().getHour();

        webtoonRedisRepository.findAll().forEach(webtoonRedis ->
            asyncService.asyncUpdateWebtoonView(webtoonRedis, curHour));
    }
}
