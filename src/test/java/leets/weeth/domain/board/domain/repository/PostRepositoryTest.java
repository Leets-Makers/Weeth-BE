package leets.weeth.domain.board.domain.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import leets.weeth.config.TestContainersConfig;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
import leets.weeth.global.config.QuerydslConfig;

@DataJpaTest
@Import({TestContainersConfig.class , QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {

	@Autowired
	private PostRepository postRepository;

	@BeforeEach
	void setUp() {
		Post post1 = Post.builder()
			.title("dsl 테스트 제목")
			.content("QueryDSL동작확인")
			.category(Category.StudyLog)
			.cardinalNumber(1)
			.week(1)
			.parts(List.of(Part.BE))
			.build();

		Post post2 = Post.builder()
			.title("테스트 제목2")
			.content("2동작2확인2")
			.category(Category.Article)
			.cardinalNumber(2)
			.week(1)
			.parts(List.of(Part.BE))
			.build();

		postRepository.saveAll(List.of(post1,post2));

	}


	@Test
	@DisplayName("findRecentPart(): StudyLog 또는 Article 카테고리 게시글 최신순 조회")
	void findRecentPart_success() {
		//given
		Pageable pageable = PageRequest.of(0, 10);

		//when
		var result = postRepository.findRecentPart(pageable);

		//then
		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent().get(0).getTitle()).isEqualTo("테스트 제목2");
	}

	@Test
	@DisplayName("searchPart(): 키워드 기반 검색")
	void searchPart_success() {
		//given
		Pageable pageable = PageRequest.of(0, 10);

		//when
		var result = postRepository.searchPart("dsl", pageable);

		//then
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getTitle()).isEqualTo("dsl 테스트 제목");
	}
}

