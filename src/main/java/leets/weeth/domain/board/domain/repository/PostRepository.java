package leets.weeth.domain.board.domain.repository;

import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Part;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {

	Slice<Post> findPageBy(Pageable page);

	Slice<Post> findPageByPart(Part part, Pageable pageable);

	Slice<Post> findByTitleContainingOrContentContainingIgnoreCase(String keyword1, String keyword2, Pageable pageable);
}
