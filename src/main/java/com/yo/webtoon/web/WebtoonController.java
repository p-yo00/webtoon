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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/webtoon"))
@RequiredArgsConstructor
public class WebtoonController {

    private final WebtoonService webtoonService;

    /**
     * 작가가 새로운 웹툰을 등록한다.
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<SuccessResponse> createWebtoon(@LoginUser String loginId,
        @Valid WebtoonDto.Create request) {
        request.setUserId(loginId);
        webtoonService.createWebtoon(request);

        return ResponseEntity.ok(SuccessResponse.toSuccessResponse(SuccessCode.CREATE_WEBTOON));
    }
}
