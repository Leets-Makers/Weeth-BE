package leets.weeth.domain.board.domain.repository;

import java.util.Collection;
import java.util.List;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("""
        SELECT p FROM Post p
        WHERE p.category IN (
            leets.weeth.domain.board.domain.entity.enums.Category.StudyLog,
            leets.weeth.domain.board.domain.entity.enums.Category.Article
        )
        ORDER BY p.id DESC
    """)
	Slice<Post> findRecentPart(Pageable pageable);

	@Query("""
        SELECT p FROM Post p
        WHERE p.category = leets.weeth.domain.board.domain.entity.enums.Category.Education
        ORDER BY p.id DESC
    """)
	Slice<Post> findRecentEducation(Pageable pageable);

	@Query("""
        SELECT p FROM Post p
        WHERE p.category IN (
            leets.weeth.domain.board.domain.entity.enums.Category.StudyLog,
            leets.weeth.domain.board.domain.entity.enums.Category.Article
        )
          AND (
                LOWER(p.title)   LIKE LOWER(CONCAT('%', :kw, '%'))
             OR LOWER(p.content) LIKE LOWER(CONCAT('%', :kw, '%'))
          )
        ORDER BY p.id DESC
    """)
	Slice<Post> searchPart(@Param("kw") String kw, Pageable pageable);

	@Query("""
        SELECT p FROM Post p
        WHERE p.category = leets.weeth.domain.board.domain.entity.enums.Category.Education
          AND (
                LOWER(p.title)   LIKE LOWER(CONCAT('%', :kw, '%'))
             OR LOWER(p.content) LIKE LOWER(CONCAT('%', :kw, '%'))
          )
        ORDER BY p.id DESC
    """)
	Slice<Post> searchEducation(@Param("kw") String kw, Pageable pageable);

	@Query("""
		SELECT DISTINCT p.studyName
		FROM Post p
		WHERE (:part = leets.weeth.domain.board.domain.entity.enums.Part.ALL OR p.part = :part)
		  AND p.studyName IS NOT NULL
		ORDER BY p.studyName ASC
	""")
	List<String> findDistinctStudyNamesByPart(@Param("part") Part part);

	@Query("""
        SELECT p
          FROM Post p
         WHERE (p.part = :part OR p.part = leets.weeth.domain.board.domain.entity.enums.Part.ALL OR :part = leets.weeth.domain.board.domain.entity.enums.Part.ALL
         )
           AND (:category IS NULL OR p.category = :category)
           AND (:cardinal IS NULL OR p.cardinalNumber = :cardinal)
           AND (:studyName IS NULL OR p.studyName = :studyName)
           AND (:week IS NULL OR p.week = :week)
      ORDER BY p.id DESC
    """)
	Slice<Post> findByPartAndOptionalFilters(@Param("part") Part part, @Param("category") Category category, @Param("cardinal") Integer cardinal, @Param("studyName") String studyName, @Param("week") Integer week, Pageable pageable);

	@Query("""
		SELECT p
		  FROM Post p
		 WHERE p.category = :category
		   AND (
				 :partName = 'ALL'
			  OR FUNCTION('FIND_IN_SET', :partName, p.parts) > 0
			  OR FUNCTION('FIND_IN_SET', 'ALL',    p.parts) > 0
			 )
	  ORDER BY p.id DESC
	""")
	Slice<Post> findByCategoryWithPart(@Param("partName") String partName, @Param("category") Category category, Pageable pageable);

	@Query("""
		SELECT p
		  FROM Post p
		 WHERE p.category = :category
		   AND p.cardinalNumber = :cardinal
		   AND (
				 :partName = 'ALL'
			  OR FUNCTION('FIND_IN_SET', :partName, p.parts) > 0
			  OR FUNCTION('FIND_IN_SET', 'ALL',    p.parts) > 0
			 )
	  ORDER BY p.id DESC
	""")
	Slice<Post> findByCategoryAndCardinalNumberWithPart(@Param("partName") String partName, @Param("category") Category category, @Param("cardinal") Integer cardinal, Pageable pageable);

	@Query("""
		SELECT p
		  FROM Post p
		 WHERE p.category = :category
		   AND p.cardinalNumber IN :cardinals
		   AND (
				 :partName = 'ALL'
			  OR FUNCTION('FIND_IN_SET', :partName, p.parts) > 0
			  OR FUNCTION('FIND_IN_SET', 'ALL',    p.parts) > 0
			 )
	  ORDER BY p.id DESC
	""")
	Slice<Post> findByCategoryAndCardinalInWithPart(@Param("partName") String partName, @Param("category") Category category, @Param("cardinals") Collection<Integer> cardinals, Pageable pageable);
}
