package leets.weeth.domain.comment.test.fixture;

import leets.weeth.domain.board.domain.entity.Notice;
import leets.weeth.domain.comment.domain.entity.Comment;
import leets.weeth.domain.user.domain.entity.User;

public class CommentTestFixture {
    public static Comment createComment(String content, User user, Notice noice){
        return Comment.builder()
                .content(content)
                .notice(noice)
                .user(user)
                .isDeleted(Boolean.FALSE)
                .build();
    }
}
