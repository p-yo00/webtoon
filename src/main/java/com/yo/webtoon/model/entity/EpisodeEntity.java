package com.yo.webtoon.model.entity;

import com.yo.webtoon.model.dto.EpisodeDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "episode")
@EntityListeners(AuditingEntityListener.class)
public class EpisodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long webtoonId;
    private int episodeNum;
    private String title;
    private String thumbnailUrl;
    private LocalDateTime uploadDt;
    @CreatedDate
    private LocalDateTime createdDt;
    @LastModifiedDate
    private LocalDateTime updatedDt;
    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL)
    private List<EpisodeContentEntity> episodeContents;

    public static EpisodeEntity toEntity(EpisodeDto.Upload episodeDto, int episodeNum) {
        return EpisodeEntity.builder()
            .webtoonId(episodeDto.getWebtoonId())
            .title(episodeDto.getTitle())
            .thumbnailUrl(episodeDto.getThumbnailImgUrl())
            .uploadDt(episodeDto.getUploadDt())
            .episodeNum(episodeNum)
            .episodeContents(episodeDto.getEpisodeContents())
            .build();
    }
}
