package com.yo.webtoon.repository;

import com.yo.webtoon.model.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(String userId);

    Optional<UserEntity> findByUserIdAndDeleteDatetime(String userId, LocalDateTime deleteDt);

    boolean existsByUserId(String userId);
}
