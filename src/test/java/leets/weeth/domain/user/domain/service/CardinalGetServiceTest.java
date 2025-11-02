package leets.weeth.domain.user.domain.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.repository.CardinalRepository;

@ExtendWith(MockitoExtension.class)
public class CardinalGetServiceTest {

	// validateCardinal() 검증로직 확인
	@Mock
	private CardinalRepository cardinalRepository;

	@InjectMocks
	private CardinalGetService cardinalGetService;

	@Test
	@DisplayName("findByAdminSide() : 존재하지 않는 기수를 넣었을 때 새로 저장되는지 확인")
	void findByAdminSide() {
		//given
		given(cardinalRepository.findByCardinalNumber(7))
			.willReturn(Optional.empty());

		given(cardinalRepository.save(any(Cardinal.class))).
			willReturn(Cardinal.builder().cardinalNumber(7).build());

		//when
		Cardinal result = cardinalGetService.findByAdminSide(7);

		//then
		assertThat(result.getCardinalNumber()).isEqualTo(7);
	}

}
