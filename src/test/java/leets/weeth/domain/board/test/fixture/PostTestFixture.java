package leets.weeth.domain.board.test.fixture;

import leets.weeth.domain.board.application.dto.PostDTO;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
import leets.weeth.domain.user.domain.entity.enums.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public static Post createEducationPost(Long id, String title, Category category, List<Part> parts,
                                           int cardinalNumber, int week){
        return Post.builder()
                .id(id)
                .title(title)
                .content("내용")
                .parts(parts)
                .cardinalNumber(cardinalNumber)
                .week(week)
                .commentCount(0)
                .category(Category.Education)
                .comments(new ArrayList<>())
                .build();
    }

    public static PostDTO.ResponseAll createResponseAll(Post post){
        return PostDTO.ResponseAll.builder()
                .id(post.getId())
                .part(post.getPart())
                .role(Role.USER)
                .title(post.getTitle())
                .content(post.getContent())
                .studyName(post.getStudyName())
                .week(post.getWeek())
                .time(LocalDateTime.now())
                .commentCount(post.getCommentCount())
                .hasFile(false)
                .isNew(false)
                .build();
    }

}
