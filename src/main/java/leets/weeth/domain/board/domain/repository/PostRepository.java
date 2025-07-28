package leets.weeth.domain.board.domain.repository;

import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PostRepository extends JpaRepository<Post, Long> {

	Slice<Post> findPageBy(Pageable page);

	@Query("""
        SELECT p
          FROM Post p
         WHERE (p.part = :part
             	OR p.part = leets.weeth.domain.board.domain.entity.enums.Part.ALL)
           AND (:category IS NULL OR p.category = :category)
           AND (:cardinal IS NULL OR p.cardinalNumber = :cardinal)
           AND (:studyName  IS NULL OR p.studyName = :studyName)
           AND (:week     IS NULL OR p.week = :week)
      ORDER BY p.id DESC
    """)
	Slice<Post> findByPartAndOptionalFilters(
			@Param("part")     Part part,
			@Param("category") Category category,
			@Param("cardinal") Integer cardinal,
			@Param("studyName")  String studyName,
			@Param("week")     Integer week,
			Pageable pageable
	);

	Slice<Post> findByCategoryAndCardinalNumber(Category category, Integer  cardinalNumber, Pageable pageable);

	Slice<Post> findByTitleContainingOrContentContainingIgnoreCase(String keyword1, String keyword2, Pageable pageable);
}
