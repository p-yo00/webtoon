package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.WebtoonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebtoonRepository extends JpaRepository<WebtoonEntity, Long> {

}
