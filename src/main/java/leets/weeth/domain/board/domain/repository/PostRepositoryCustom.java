package leets.weeth.domain.board.domain.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;

public interface PostRepositoryCustom {

	Slice<Post> findRecentPart(Pageable pageable);
	Slice<Post> findRecentEducation(Pageable pageable);
	Slice<Post> searchPart(String kw,Pageable pageable);
	Slice<Post> searchEducation(String kw, Pageable pageable);
	List<String> findDistinctStudyNamesByPart(Part part);
	Slice<Post> findByPartAndOptionalFilters(Part part, Category category, Integer cardinal, String studyName, Integer week, Pageable pageable);
	Slice<Post> findByCategoryAndOptionalCardinalWithPart(String partName, Category category, Integer cardinal, Pageable pageable);
	Slice<Post> findByCategoryAndCardinalNumberWithPart(String partName, Category category, Integer cardinal, Pageable pageable);
	Slice<Post> findByCategoryAndCardinalInWithPart(String partName, Category category,  Collection<Integer> cardinals, Pageable pageable);

}
