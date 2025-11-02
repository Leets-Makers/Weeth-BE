package leets.weeth.domain.user.domain.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import leets.weeth.domain.user.domain.entity.UserCardinal;
import leets.weeth.domain.user.domain.repository.UserCardinalRepository;
import leets.weeth.domain.user.test.fixture.CardinalTestFixture;
import leets.weeth.domain.user.test.fixture.UserTestFixture;

@ExtendWith(MockitoExtension.class)
public class UserCardinalGetServiceTest {

	@Mock
	private UserCardinalRepository userCardinalRepository;

	@InjectMocks
	private UserCardinalGetService userCardinalGetService;


	@Test
	@DisplayName("notContains() : 유저의 기수 목록 중, 특정 기수가 없는지 확인 ")
	void notContains() {
		//given
		var user = UserTestFixture.createActiveUser1();
		var existingCardinal = CardinalTestFixture.createCardinal(7,2025,2);
		var targetCardinal = CardinalTestFixture.createCardinal(8,2026,1);
		var userCardinal = new UserCardinal(user, existingCardinal);

		given(userCardinalRepository.findAllByUserOrderByCardinalCardinalNumberDesc(user))
			.willReturn(List.of(userCardinal));
		//when
		boolean result = userCardinalGetService.notContains(user, targetCardinal);

		//then
		assertThat(result).isTrue();
	}

}
