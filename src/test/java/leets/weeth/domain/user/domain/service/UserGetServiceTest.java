package leets.weeth.domain.user.domain.service;

import leets.weeth.domain.user.application.exception.UserNotFoundException;
import leets.weeth.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserGetServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserGetService userGetService;

	@Test
	@DisplayName("find(Long Id) : 존재하지 않는 유저일 때 예외를 던진다")
	void find_id_userNotFound_throwsException() {
		//given
		Long userId = Long.valueOf(1L);
		given(userRepository.findById(userId)).willReturn(Optional.empty());

		// when & then
		assertThrows(UserNotFoundException.class, () -> userGetService.find(userId));
	}

	@Test
	@DisplayName("find(String email) : 존재하지 않는 유저일 때 예외를 던진다")
	void find_email_userNotFound_throwsException() {
		//given
		String email = "test@test.com";
		given(userRepository.findByEmail(email)).willReturn(Optional.empty());

		//when & then
		assertThrows(UserNotFoundException.class, () -> userGetService.find(email));
	}

}
