package leets.weeth.domain.user.test.fixture;

import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.entity.enums.Status;

public class UserTestFixture {

	public static User createActiveUser() {
		return User.builder()
			.name("적순")
			.email("test1@test.com")
			.status(Status.ACTIVE)
			.build();
	}

	public static User createActiveUser(Long id) {
		return User.builder()
			.id(id)
			.name("적순")
			.email("test1@test.com")
			.status(Status.ACTIVE)
			.build();
	}

	public static User createWaitingUser() {
		return User.builder()
			.name("순적")
			.email("test2@test.com")
			.status(Status.WAITING)
			.build();
	}

	public static User createWaitingUser(Long id) {
		return User.builder()
			.id(id)
			.name("순적")
			.email("test2@test.com")
			.status(Status.WAITING)
			.build();
	}


}
