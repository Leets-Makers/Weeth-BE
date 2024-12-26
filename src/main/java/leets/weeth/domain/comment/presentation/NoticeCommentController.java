package leets.weeth.domain.comment.presentation;

import static leets.weeth.domain.comment.presentation.ResponseMessage.COMMENT_CREATED_SUCCESS;
import static leets.weeth.domain.comment.presentation.ResponseMessage.COMMENT_DELETED_SUCCESS;
import static leets.weeth.domain.comment.presentation.ResponseMessage.COMMENT_UPDATED_SUCCESS;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.comment.application.dto.CommentDTO;
import leets.weeth.domain.comment.application.usecase.NoticeCommentUsecase;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.domain.user.application.exception.UserNotMatchException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "COMMENT-NOTICE", description = "공지사항 댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices/{noticeId}/comments")
public class NoticeCommentController {

    private final NoticeCommentUsecase noticeCommentUsecase;

    @PostMapping
    @Operation(summary="공지사항 댓글 작성")
    public CommonResponse<String> saveNoticeComment(@RequestBody @Valid CommentDTO.Save dto, @PathVariable Long noticeId,
                                                    @Parameter(hidden = true) @CurrentUser Long userId) {
        noticeCommentUsecase.saveNoticeComment(dto, noticeId, userId);
        return CommonResponse.createSuccess(COMMENT_CREATED_SUCCESS.getMessage());
    }

    @PatchMapping("{commentId}")
    @Operation(summary="공지사항 댓글 수정")
    public CommonResponse<String> updateNoticeComment(@RequestBody @Valid CommentDTO.Update dto, @PathVariable Long noticeId,
                                                      @PathVariable Long commentId,@Parameter(hidden = true) @CurrentUser Long userId) throws UserNotMatchException {
        noticeCommentUsecase.updateNoticeComment(dto, noticeId, commentId, userId);
        return CommonResponse.createSuccess(COMMENT_UPDATED_SUCCESS.getMessage());
    }

    @DeleteMapping("{commentId}")
    @Operation(summary="공지사항 댓글 삭제")
    public CommonResponse<String> deleteNoticeComment(@PathVariable Long commentId,
                                                      @Parameter(hidden = true) @CurrentUser Long userId) throws UserNotMatchException {
        noticeCommentUsecase.deleteNoticeComment(commentId, userId);
        return CommonResponse.createSuccess(COMMENT_DELETED_SUCCESS.getMessage());
    }

}
