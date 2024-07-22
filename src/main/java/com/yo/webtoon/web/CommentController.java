package com.yo.webtoon.web;

import com.yo.webtoon.model.dto.CommentDto;
import com.yo.webtoon.model.dto.LoginUser;
import com.yo.webtoon.model.dto.SuccessResponse;
import com.yo.webtoon.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 에피소드에 댓글을 작성한다.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> postComment(@LoginUser Long loginId,
        @RequestBody CommentDto.Post comment) {
        comment.setUserId(loginId);
        commentService.postComment(comment);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(null);
    }

    /**
     * 댓글에 추천(좋아요)을 한다.
     */
    @PutMapping("/good/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse> recommendComment(@LoginUser Long loginId,
        @PathVariable("commentId") Long commentId) {
        commentService.recommendComment(loginId, commentId);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(null);
    }
}
