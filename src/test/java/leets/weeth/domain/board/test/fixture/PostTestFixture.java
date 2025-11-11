package leets.weeth.domain.board.test.fixture;

import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Category;

import java.util.ArrayList;

public class PostTestFixture {
    public static Post createPost(Long id, String title, Category category){
        return Post.builder()
                .id(id)
                .title(title)
                .content("내용")
                .comments(new ArrayList<>())
                .commentCount(0)
                .category(category)
                .build();
    }

}
