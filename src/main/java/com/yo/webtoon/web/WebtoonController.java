package com.yo.webtoon.web;

import com.yo.webtoon.model.constant.SuccessCode;
import com.yo.webtoon.model.dto.LoginUser;
import com.yo.webtoon.model.dto.SuccessResponse;
import com.yo.webtoon.model.dto.WebtoonDto;
import com.yo.webtoon.service.WebtoonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/webtoon"))
@RequiredArgsConstructor
public class WebtoonController {

    private final WebtoonService webtoonService;

    /**
     * Form-data로 입력받아 작가가 새로운 웹툰을 등록한다.
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<SuccessResponse> createWebtoon(@LoginUser Long loginId,
        @Valid WebtoonDto.Create request) {
        request.setUserId(loginId);
        webtoonService.createWebtoon(request);

        return ResponseEntity.ok(SuccessResponse.toSuccessResponse(SuccessCode.CREATE_WEBTOON));
    }

    /**
     * Form-data로 입력받아 작가가 웹툰 정보(소개글, 키워드, 이미지, 업로드주기, 후원알림)를 수정한다.
     */
    @PutMapping("/info/{webtoonId}")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<SuccessResponse> updateWebtoonInfo(@LoginUser Long loginId,
        @Valid WebtoonDto.EditInfo request,
        @PathVariable("webtoonId") Long webtoonId) {
        request.setUserId(loginId);
        request.setWebtoonId(webtoonId);
        webtoonService.updateWebtoonInfo(request);

        return ResponseEntity.ok(SuccessResponse.toSuccessResponse(SuccessCode.UPDATE_WEBTOON));
    }

    /**
     * Form-data로 입력받아 관리자가 웹툰 설정(제목, 장르, 연령제한)을 수정한다.
     */
    @PutMapping("/config/{webtoonId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<SuccessResponse> updateWebtoonConfig(@Valid WebtoonDto.EditConfig request,
        @PathVariable("webtoonId") Long webtoonId) {
        request.setWebtoonId(webtoonId);
        webtoonService.updateWebtoonConfig(request);

        return ResponseEntity.ok(SuccessResponse.toSuccessResponse(SuccessCode.UPDATE_WEBTOON));
    }
}
