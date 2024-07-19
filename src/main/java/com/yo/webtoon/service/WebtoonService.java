package com.yo.webtoon.service;

import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.dto.WebtoonDto.Create;
import com.yo.webtoon.model.dto.WebtoonDto.EditConfig;
import com.yo.webtoon.model.dto.WebtoonDto.EditInfo;
import com.yo.webtoon.model.dto.WebtoonIndexDto;
import com.yo.webtoon.model.entity.UserEntity;
import com.yo.webtoon.model.entity.WebtoonEntity;
import com.yo.webtoon.repository.UserRepository;
import com.yo.webtoon.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
