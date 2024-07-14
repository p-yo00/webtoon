package com.yo.webtoon.model.entity;

import com.yo.webtoon.model.constant.Role;
import com.yo.webtoon.model.dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@SQLRestriction("delete_datetime is null")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String password;
    private String userName;
    @Builder.Default
    private int point = 0;
    @CreatedDate
    private LocalDateTime registerDatetime;
    private LocalDateTime deleteDatetime;
    private LocalDate adultCertificationDate;
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public static UserEntity toEntity(UserDto.SignUp userDto) {
        return UserEntity.builder()
            .userId(userDto.getUserId())
            .password(userDto.getPassword())
            .userName(userDto.getUserName())
            .adultCertificationDate((userDto.isAdult()) ? LocalDate.now() : null)
            .role((userDto.getRole() == null) ? Role.ROLE_GENERAL : Role.valueOf(userDto.getRole()))
            .build();
    }
}
