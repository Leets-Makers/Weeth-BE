package leets.weeth.domain.user.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import leets.weeth.config.TestContainersConfig;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.entity.UserCardinal;
import leets.weeth.domain.user.domain.entity.enums.Status;

@DataJpaTest
@Import(TestContainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsesrCardinalRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	CardinalRepository cardinalRepository;

	@Autowired
	UserCardinalRepository userCardinalRepository;

	@Test
	void 유저별_기수_내림차순_조회되는지() {
		//given
		User user = User.builder()
			.email("test@test.com")
			.name("문적순")
			.status(Status.ACTIVE)
			.build();

		userRepository.save(user);

		Cardinal cardinal1 = cardinalRepository.save(Cardinal.builder()
			.cardinalNumber(5)
			.year(2023)
			.semester(1)
			.build());

		Cardinal cardinal2 = cardinalRepository.save(Cardinal.builder()
			.cardinalNumber(6)
			.year(2023)
			.semester(2)
			.build());

		Cardinal cardinal3 = cardinalRepository.save(Cardinal.builder()
			.cardinalNumber(7)
			.year(2024)
			.semester(1)
			.build());


		userCardinalRepository.saveAll(List.of(
			new UserCardinal(user, cardinal1),
			new UserCardinal(user, cardinal2),
			new UserCardinal(user, cardinal3)
		));

		//when
		List<UserCardinal> result = userCardinalRepository.findAllByUserOrderByCardinalCardinalNumberDesc(user);

		//then
		assertThat(result).hasSize(3);
		assertThat(result.get(0).getCardinal().getCardinalNumber()).isEqualTo(7);
		assertThat(result.get(1).getCardinal().getCardinalNumber()).isEqualTo(6);
		assertThat(result.get(2).getCardinal().getCardinalNumber()).isEqualTo(5);

	}


}
