package com.yo.webtoon.web;

import com.yo.webtoon.model.constant.SuccessCode;
import com.yo.webtoon.model.dto.GetWebtoonParams;
import com.yo.webtoon.model.dto.LoginUser;
import com.yo.webtoon.model.dto.SuccessResponse;
import com.yo.webtoon.model.dto.WebtoonDto;
import com.yo.webtoon.model.dto.WebtoonIndexDto;
import com.yo.webtoon.service.WebtoonService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    /**
     * 연재중인 웹툰을 완결 상태로 변경한다.
     */
    @PutMapping("/complete/{webtoonId}")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<SuccessResponse> completeWebtoon(@LoginUser Long loginId,
        @PathVariable("webtoonId") Long webtoonId) {
        webtoonService.completeWebtoon(loginId, webtoonId);

        return ResponseEntity.ok(SuccessResponse.toSuccessResponse(SuccessCode.UPDATE_WEBTOON));
    }

    /**
     * 웹툰의 공개 상태를 공개 또는 비공개로 변경한다. (기존 상태와 반대로 토글)
     */
    @PutMapping("/public/{webtoonId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<SuccessResponse> openOrCloseWebtoon(
        @PathVariable("webtoonId") Long webtoonId) {
        webtoonService.openOrCloseWebtoon(webtoonId);

        return ResponseEntity.ok(SuccessResponse.toSuccessResponse(SuccessCode.UPDATE_WEBTOON));
    }

    /**
     * 웹툰을 조건에 따라 분류 및 정렬하여 조회한다. order(정렬 조건): Order, complete(완결여부): boolean, genre(장르):Genre
     */
    @GetMapping
    public ResponseEntity<List<WebtoonIndexDto>> getWebtoon(
        @ModelAttribute GetWebtoonParams webtoonParams,
        Pageable pageable) {

        return ResponseEntity.ok(webtoonService.getWebtoon(webtoonParams, pageable));
    }

    /**
     * 웹툰을 wishlist에 등록한다.
     */
    @PostMapping("/wishlist/{webtoonId}")
    @PreAuthorize("hasAnyRole('ROLE_GENERAL','ROLE_AUTHOR')")
    public ResponseEntity<SuccessResponse> createWishlist(@LoginUser Long loginId,
        @PathVariable("webtoonId") Long webtoonId) {
        webtoonService.createWishlist(loginId, webtoonId);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(SuccessResponse.toSuccessResponse(SuccessCode.ADD_WISHLIST));
    }

    /**
     * wishlist에 등록된 웹툰을 wishlist에서 삭제한다.
     */
    @DeleteMapping("/wishlist/{webtoonId}")
    @PreAuthorize("hasAnyRole('ROLE_GENERAL','ROLE_AUTHOR')")
    public ResponseEntity<SuccessResponse> deleteWishlist(@LoginUser Long loginId,
        @PathVariable("webtoonId") Long webtoonId) {
        webtoonService.deleteWishlist(loginId, webtoonId);

        return ResponseEntity.ok(SuccessResponse.toSuccessResponse(SuccessCode.REMOVE_WISHLIST));
    }
}
