package leets.weeth.domain.board.domain.entity;

import jakarta.persistence.*;
import leets.weeth.domain.board.application.dto.NoticeDTO;
import leets.weeth.domain.board.application.dto.PostDTO;
import leets.weeth.domain.comment.domain.entity.Comment;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.List;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer commentCount;

    @PrePersist
    public void prePersist() {
        commentCount = 0;
    }

    public void decreaseCommentCount() {
        if (commentCount > 0) {
            commentCount--;
        }
    }

    public void updateCommentCount(List<Comment> comments) {
        this.commentCount = (int) comments.stream()
                .filter(comment -> !comment.getIsDeleted())
                .count();
    }

    public void updateUpperClass(NoticeDTO.Update dto) {
        this.title = dto.title();
        this.content = dto.content();
    }

    public void updateUpperClass(PostDTO.Update dto) {
        this.title = dto.title();
        this.content = dto.content();
    }

}
