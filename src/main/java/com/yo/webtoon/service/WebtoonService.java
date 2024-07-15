package com.yo.webtoon.service;

import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.dto.WebtoonDto.Create;
import com.yo.webtoon.model.dto.WebtoonIndexDto;
import com.yo.webtoon.model.entity.UserEntity;
import com.yo.webtoon.model.entity.WebtoonEntity;
import com.yo.webtoon.repository.UserRepository;
import com.yo.webtoon.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        UserEntity loginUser = userRepository.findByUserId(newWebtoon.getUserId()).orElseThrow(
            () -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        WebtoonEntity webtoonEntity = WebtoonEntity.toEntity(newWebtoon, loginUser.getId());
        webtoonRepository.save(webtoonEntity);

        String imgKey = "webtoon_" + webtoonEntity.getId();
        String imgUrl = amazonS3Service.getImgUrl(imgKey);
        webtoonEntity.setImgUrl(imgUrl);

        amazonS3Service.putObject(imgKey, newWebtoon.getImg());

        elasticsearchService.saveToWebtoonIndex(
            new WebtoonIndexDto(webtoonEntity, newWebtoon.getUserId()));
    }
}
