package com.yo.webtoon.web;

import com.yo.webtoon.model.constant.SuccessCode;
import com.yo.webtoon.model.dto.EpisodeDto;
import com.yo.webtoon.model.dto.EpisodeDto.ImgUrls;
import com.yo.webtoon.model.dto.LoginUser;
import com.yo.webtoon.model.dto.SuccessResponse;
import com.yo.webtoon.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/episode")
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;

    /**
     * form data를 받아 웹툰에 에피소드를 업로드한다.
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<SuccessResponse> uploadEpisode(
        @LoginUser Long loginId,
        EpisodeDto.Upload episode) {
        episodeService.uploadEpisode(episode, loginId);

        return ResponseEntity
            .status(SuccessCode.CREATE_EPISODE.getHttpStatus())
            .body(SuccessResponse.toSuccessResponse(SuccessCode.CREATE_EPISODE));
    }

    /**
     * 웹툰의 특정 에피소드를 조회한다.
     */
    @GetMapping("/{webtoonId}/{episodeNum}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ImgUrls> readEpisode(
        @LoginUser Long loginId,
        @PathVariable("webtoonId") Long webtoonId,
        @PathVariable("episodeNum") int episodeNum) {

        return ResponseEntity.ok(episodeService.readEpisode(loginId, webtoonId, episodeNum));
    }
}
