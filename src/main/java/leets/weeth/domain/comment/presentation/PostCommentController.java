package leets.weeth.domain.comment.presentation;

import static leets.weeth.domain.comment.presentation.ResponseMessage.POST_COMMENT_CREATED_SUCCESS;
import static leets.weeth.domain.comment.presentation.ResponseMessage.POST_COMMENT_DELETED_SUCCESS;
import static leets.weeth.domain.comment.presentation.ResponseMessage.POST_COMMENT_UPDATED_SUCCESS;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.comment.application.dto.CommentDTO;
import leets.weeth.domain.comment.application.usecase.PostCommentUsecase;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.domain.user.application.exception.UserNotMatchException;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "COMMENT-BOARD", description = "게시판 댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board/{boardId}/comments")
public class PostCommentController {

    private final PostCommentUsecase postCommentUsecase;

    @PostMapping
    @Operation(summary="게시글 댓글 작성")
    public CommonResponse<String> savePostComment(@RequestBody @Valid CommentDTO.Save dto, @PathVariable Long boardId,
                                                  @Parameter(hidden = true) @CurrentUser Long userId) {
        postCommentUsecase.savePostComment(dto, boardId, userId);
        return CommonResponse.createSuccess(POST_COMMENT_CREATED_SUCCESS.getMessage());
    }

    @PatchMapping("/{commentId}")
    @Operation(summary="게시글 댓글 수정")
    public CommonResponse<String> updatePostComment(@RequestBody @Valid CommentDTO.Update dto, @PathVariable Long boardId, @PathVariable Long commentId,
                                                    @Parameter(hidden = true) @CurrentUser Long userId) throws UserNotMatchException {
        postCommentUsecase.updatePostComment(dto, boardId, commentId, userId);
        return CommonResponse.createSuccess(POST_COMMENT_UPDATED_SUCCESS.getMessage());
    }

    @DeleteMapping("{commentId}")
    @Operation(summary="게시글 댓글 삭제")
    public CommonResponse<String> deletePostComment(@PathVariable Long commentId, @Parameter(hidden = true) @CurrentUser Long userId) throws UserNotMatchException {
        postCommentUsecase.deletePostComment(commentId, userId);
        return CommonResponse.createSuccess(POST_COMMENT_DELETED_SUCCESS.getMessage());
    }

}
