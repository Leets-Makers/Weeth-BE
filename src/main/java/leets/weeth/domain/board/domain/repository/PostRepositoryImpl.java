package leets.weeth.domain.board.domain.repository;

import java.beans.Expression;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.QPost;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
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

	 @Override
	 public Slice<Post> findRecentEducation(Pageable pageable) {
		List<Post> result = queryFactory
			.selectFrom(post)
			.where(post.category.eq(Category.Education))
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
	public Slice<Post> searchEducation(String kw, Pageable pageable) {
		List<Post> result = queryFactory
			.selectFrom(post)
			.where(
				post.category.eq(Category.Education)
					.and(
						post.title.containsIgnoreCase(kw)
							.or(post.content.containsIgnoreCase(kw))
					)
			)
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
	public List<String> findDistinctStudyNamesByPart(Part part) {

		 return queryFactory
			 .select(post.studyName)
			 .distinct()
			 .from(post)
			 .where(
				 part == Part.ALL
					 ? post.studyName.isNotNull()
					 : post.part.eq(part).and(post.studyName.isNotNull())
			 )
			 .orderBy(post.studyName.asc())
			 .fetch();
	}

	@Override //조건이 많은 쿼리라 BooleanBuilder 사용
	public Slice<Post> findByPartAndOptionalFilters(Part part, Category category, Integer cardinal, String studyName, Integer week, Pageable pageable) {

		BooleanBuilder builder = new BooleanBuilder();

		if (part == Part.ALL) {
			builder.and(post.part.eq(Part.ALL).or(post.part.ne(Part.ALL)));
		}else {
			builder.and(post.part.eq(part).or(post.part.eq(Part.ALL)));
		}

		if (category != null) {
			builder.and(post.category.eq(category));
		}

		if (cardinal != null) {
			builder.and(post.cardinalNumber.eq(cardinal));
		}

		if (studyName != null) {
			builder.and(post.studyName.eq(studyName));
		}

		if (week != null) {
			builder.and(post.week.eq(week));
		}

		List<Post> result = queryFactory
			.selectFrom(post)
			.where(builder)
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
	public Slice<Post> findByCategoryAndOptionalCardinalWithPart(String partName, Category category, Integer cardinal, Pageable pageable) {

		BooleanBuilder builder = new BooleanBuilder();

		builder.and(post.category.eq(category));

		if (cardinal != null) {
			builder.and(post.cardinalNumber.eq(cardinal));
		}

		if (!"ALL".equals(partName)) { //partName 조건
			builder.and(
				Expressions.numberTemplate(Integer.class,
						"FIND_IN_SET({0}, {1})", partName, post.parts).gt(0)
					.or(Expressions.numberTemplate(Integer.class,
						"FIND_IN_SET('ALL', {0})", post.parts).gt(0))
			);
		}

		List<Post> result = queryFactory
			.selectFrom(post)
			.where(builder)
			.orderBy(post.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();
		boolean hasNext = result.size() > pageable.getPageSize();
		if (hasNext) result.remove(pageable.getPageSize());

		return new SliceImpl<>(result, pageable, hasNext);
	}

	@Override
	public Slice<Post> findByCategoryAndCardinalNumberWithPart(String partName, Category category, Integer cardinal, Pageable pageable) {

		BooleanBuilder builder = new BooleanBuilder();

		builder.and(post.category.eq(category));
		builder.and(post.cardinalNumber.eq(cardinal));

		if (!"ALL".equals(partName)) {
			BooleanExpression containsPart =
				Expressions.numberTemplate(Integer.class, "FIND_IN_SET({0}, {1})", partName, post.parts).gt(0);

			BooleanExpression containsAll =
				Expressions.numberTemplate(Integer.class,
					"FIND_IN_SET('ALL', {0})", post.parts).gt(0);

			builder.and(containsPart.or(containsAll));
		}

		List<Post> result = queryFactory
			.selectFrom(post)
			.where(builder)
			.orderBy(post.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = result.size() > pageable.getPageSize();
		if (hasNext) result.remove(pageable.getPageSize());

		return new SliceImpl<>(result, pageable, hasNext);
	}

	@Override
	public Slice<Post> findByCategoryAndCardinalInWithPart(String partName, Category category,  Collection<Integer> cardinals, Pageable pageable) {
		BooleanBuilder builder = new BooleanBuilder();

		builder.and(post.category.eq(category));
		builder.and(post.cardinalNumber.in(cardinals));

		if (!"ALL".equals(partName)) {
			BooleanExpression containsPart =
				Expressions.numberTemplate(Integer.class,
					"FIND_IN_SET({0}, {1})", partName, post.parts).gt(0);

			BooleanExpression containsAll =
				Expressions.numberTemplate(Integer.class,
					"FIND_IN_SET('ALL', {0})", post.parts).gt(0);

			builder.and(containsPart.or(containsAll));
		}

		List<Post> result = queryFactory
			.selectFrom(post)
			.where(builder)
			.orderBy(post.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = result.size() > pageable.getPageSize();
		if (hasNext) result.remove(pageable.getPageSize());

		return new SliceImpl<>(result, pageable, hasNext);
	}
}
