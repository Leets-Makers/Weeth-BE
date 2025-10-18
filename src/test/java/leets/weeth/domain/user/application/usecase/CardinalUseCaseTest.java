package leets.weeth.domain.user.application.usecase;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import leets.weeth.domain.user.application.dto.request.CardinalSaveRequest;
import leets.weeth.domain.user.application.mapper.CardinalMapper;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.enums.CardinalStatus;
import leets.weeth.domain.user.domain.service.CardinalGetService;
import leets.weeth.domain.user.domain.service.CardinalSaveService;

@ExtendWith(MockitoExtension.class)
public class CardinalUseCaseTest {
	// 실제 CardinalUseCase에서 사용하는 의존성을 Mock 객체로 대신 주입
	@Mock
	private CardinalGetService cardinalGetService;

	@Mock
	private CardinalSaveService cardinalSaveService;

	@Mock
	private CardinalMapper cardinalMapper;

	@InjectMocks
	private CardinalUseCase useCase;

	// Given-When-Then 패턴을 쉽게 이해하기 위해 메서드명_상황_예상결과로  테스트 메서드 네이밍


	@Test // 진행중이 아닌 기수를 등록하는 경우
	void save_진행중이_아닌_기수라면_검증후_저장만() {
		//given
		var request = new CardinalSaveRequest(7, 2025,1,false);

		var toSave = Cardinal.builder() //DB저장되기 전의 객체
			.cardinalNumber(7)
			.year(2025)
			.semester(1)
			.build();

		var saved = Cardinal.builder()// 저장되고 난 후 반환된 객체
			.cardinalNumber(7)
			.year(2025)
			.semester(1)
			.status(CardinalStatus.DONE)
			.build();

		willDoNothing().given(cardinalGetService).validateCardinal(7);
		given(cardinalMapper.from(request)).willReturn(toSave);
		given(cardinalSaveService.save(toSave)).willReturn(saved);

		//when
		useCase.save(request);

		//then
		then(cardinalGetService).should().validateCardinal(7);
		then(cardinalSaveService).should().save(toSave);
		then(cardinalGetService).should(never()).findInProgress();
	}

	@Test
	void save_새_기수가_진행중이라면_기존_기수는_done_현재기수는_inProgress() {
		//given
		//when
		//then
	}

	@Test
	void update_진행상태가_변하지_않는다면_단순_업데이트만() {
		//given
		//when
		//then
	}

	@Test
	void update_진행중으로_변경되면_기존은_done처리_현재는_inProgress() {
		//given
		//when
		//then
	}

	@Test
	void finaALl_조회된_모든_기수를_DTO로_매핑처리() {

	}





}
