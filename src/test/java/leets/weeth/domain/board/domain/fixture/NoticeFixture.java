package leets.weeth.domain.board.domain.fixture;

import leets.weeth.domain.board.domain.entity.Notice;
import leets.weeth.domain.user.domain.entity.User;

public class NoticeFixture {
    public static Notice createNotice(String title, User user){
        return Notice.builder()
                .title(title)
                .content("내용")
                .user(user)
                .commentCount(0)
                .build();
    }
}
