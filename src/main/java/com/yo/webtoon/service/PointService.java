package com.yo.webtoon.service;

import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import com.yo.webtoon.model.dto.RefType;
import com.yo.webtoon.model.entity.DonationInfoEntity;
import com.yo.webtoon.model.entity.PointHistoryEntity;
import com.yo.webtoon.model.entity.UserEntity;
import com.yo.webtoon.repository.DonationInfoRepository;
import com.yo.webtoon.repository.PointHistoryRepository;
import com.yo.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserRepository userRepository;
    private final DonationInfoRepository donationInfoRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void donatePoint(Long commentId, Long userId, Integer donationAmount) {
        double COMMISSION = 0.3;
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new WebtoonException(ErrorCode.USER_NOT_FOUND));

        int curPoint = userEntity.getPoint();
        int afterPoint = userEntity.getPoint() - donationAmount;

        DonationInfoEntity donationInfoEntity = donationInfoRepository.save(
            DonationInfoEntity.builder()
                .actualAmount(donationAmount)
                .commission((int) Math.round(donationAmount * COMMISSION))
                .commentId(commentId)
                .build());

        pointHistoryRepository.save(PointHistoryEntity.builder()
            .userId(userId)
            .amount(donationAmount)
            .prevAmount(curPoint)
            .afterAmount(afterPoint)
            .refType(RefType.DONATION)
            .refId(donationInfoEntity.getId())
            .build());
    }
}
