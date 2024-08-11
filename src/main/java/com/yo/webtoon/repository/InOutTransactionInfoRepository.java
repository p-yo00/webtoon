package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.InOutTransactionInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InOutTransactionInfoRepository extends
    JpaRepository<InOutTransactionInfoEntity, Long> {

}
