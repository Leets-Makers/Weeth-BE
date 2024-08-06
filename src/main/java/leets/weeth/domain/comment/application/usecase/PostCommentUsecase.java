package leets.weeth.domain.comment.application.usecase;

import leets.weeth.domain.comment.application.dto.CommentDTO;
import leets.weeth.global.common.error.exception.custom.UserNotMatchException;

public interface PostCommentUsecase {

    void savePostComment(CommentDTO.Save dto, Long postId, Long userId);

    void updatePostComment(CommentDTO.Update dto, Long postId, Long commentId, Long userId) throws UserNotMatchException;

    void deletePostComment(Long commentId, Long userId) throws UserNotMatchException;

}
