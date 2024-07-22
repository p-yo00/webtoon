package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.DonationInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationInfoRepository extends JpaRepository<DonationInfoEntity, Long> {

}
