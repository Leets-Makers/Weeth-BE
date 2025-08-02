package leets.weeth.domain.board.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.util.List;
import leets.weeth.domain.board.application.dto.PostDTO;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
import leets.weeth.domain.comment.domain.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Post extends Board {

    @Column
    private String studyName;

    @Column(nullable = false)
    private int cardinalNumber;

    @Column(nullable=false)
    private int week;

    @Enumerated(EnumType.STRING)
    private Part part;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments;

    public void updateCommentCount() {
        this.updateCommentCount(this.comments);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void update(PostDTO.Update dto) {
        this.updateUpperClass(dto);
    }
}
