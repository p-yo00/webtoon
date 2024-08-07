package com.yo.webtoon.model.entity;

import com.yo.webtoon.model.constant.Genre;
import com.yo.webtoon.model.dto.WebtoonDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "webtoon")
@EntityListeners(AuditingEntityListener.class)
public class WebtoonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long authorId;
    private String title;
    private String description;
    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> keyword;
    private String imgUrl;
    @Enumerated(value = EnumType.STRING)
    private Genre genre;
    private boolean hasAgeLimit;
    private int uploadCycle;
    private boolean donationAlarm;
    @Builder.Default
    private Long totalViewCnt = 0L;
    @Builder.Default
    private Long recentViewCnt = 0L;
    @Builder.Default
    private boolean isPublic = false;
    @Builder.Default
    private boolean isComplete = false;
    @CreatedDate
    private LocalDateTime createDatetime;

    public static WebtoonEntity toEntity(WebtoonDto.Create webtoonDto) {
        return WebtoonEntity.builder()
            .authorId(webtoonDto.getUserId())
            .title(webtoonDto.getTitle())
            .description(webtoonDto.getDescription())
            .keyword(webtoonDto.getKeyword())
            .imgUrl(webtoonDto.getImgUrl())
            .genre(webtoonDto.getGenre())
            .hasAgeLimit(webtoonDto.isAgeLimit())
            .uploadCycle(webtoonDto.getUploadCycle())
            .donationAlarm(webtoonDto.isDonationAlarm())
            .build();
    }
}
