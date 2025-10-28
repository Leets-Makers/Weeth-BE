package leets.weeth.domain.user.test.fixture;

import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.enums.CardinalStatus;

public class CardinalTestFixture {

	public static Cardinal createCardinal() {
		return Cardinal.builder()
			.cardinalNumber(3)
			.year(2024)
			.semester(1)
			.status(CardinalStatus.DONE)
			.build();
	}

	public static Cardinal createCardinal(Long id) {
		return Cardinal.builder()
			.id(id)
			.cardinalNumber(3)
			.year(2024)
			.semester(1)
			.status(CardinalStatus.DONE)
			.build();
	}

	public static Cardinal createCardinalInProgress() {
		return Cardinal.builder()
			.cardinalNumber(3)
			.year(2024)
			.semester(1)
			.status(CardinalStatus.IN_PROGRESS)
			.build();
	}

	public static Cardinal createCardinalInProgress(Long id) {
		return Cardinal.builder()
			.id(id)
			.cardinalNumber(3)
			.year(2024)
			.semester(1)
			.status(CardinalStatus.IN_PROGRESS)
			.build();
	}
}
