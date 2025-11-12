package leets.weeth.domain.board.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.QPost;
import leets.weeth.domain.board.domain.entity.enums.Category;
import lombok.RequiredArgsConstructor;


public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final QPost post = QPost.post;

	public PostRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Slice<Post> findRecentPart(Pageable pageable) {
		List<Post> result = queryFactory
			.selectFrom(post)
			.where(post.category.in(Category.StudyLog, Category.Article))
			.orderBy(post.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = result.size() > pageable.getPageSize();

		if (hasNext) {
			result.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(result, pageable, hasNext);
	}

	@Override
	public Slice<Post> searchPart(String kw, Pageable pageable) {
		List<Post> result = queryFactory
			.selectFrom(post)
			.where(post.category.in(Category.StudyLog, Category.Article)
				.and(post.title.containsIgnoreCase(kw)
					.or(post.content.containsIgnoreCase(kw))
			))
			.orderBy(post.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = result.size() > pageable.getPageSize();

		if (hasNext) {
			result.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(result, pageable, hasNext);
 	}
}
