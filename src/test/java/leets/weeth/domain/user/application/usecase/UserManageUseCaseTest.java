package leets.weeth.domain.user.application.usecase;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import leets.weeth.domain.attendance.domain.service.AttendanceSaveService;
import leets.weeth.domain.schedule.domain.entity.Meeting;
import leets.weeth.domain.schedule.domain.service.MeetingGetService;
import leets.weeth.domain.user.application.dto.request.UserRequestDto;
import leets.weeth.domain.user.application.dto.response.UserResponseDto;
import leets.weeth.domain.user.application.exception.InvalidUserOrderException;
import leets.weeth.domain.user.application.mapper.UserMapper;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.entity.UserCardinal;
import leets.weeth.domain.user.domain.entity.enums.Status;
import leets.weeth.domain.user.domain.entity.enums.UsersOrderBy;
import leets.weeth.domain.user.domain.service.CardinalGetService;
import leets.weeth.domain.user.domain.service.UserCardinalGetService;
import leets.weeth.domain.user.domain.service.UserCardinalSaveService;
import leets.weeth.domain.user.domain.service.UserDeleteService;
import leets.weeth.domain.user.domain.service.UserGetService;
import leets.weeth.domain.user.domain.service.UserUpdateService;
import leets.weeth.global.auth.jwt.service.JwtRedisService;

@ExtendWith(MockitoExtension.class)
public class UserManageUseCaseTest {

	@Mock private UserGetService userGetService;
	@Mock private UserUpdateService userUpdateService;
	@Mock private UserDeleteService userDeleteService;

	@Mock private AttendanceSaveService attendanceSaveService;
	@Mock private MeetingGetService meetingGetService;
	@Mock private JwtRedisService jwtRedisService;
	@Mock private CardinalGetService cardinalGetService;
	@Mock private UserCardinalSaveService userCardinalSaveService;
	@Mock private UserCardinalGetService userCardinalGetService;

	@Mock private UserMapper userMapper;
	@Mock private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserManageUseCaseImpl useCase;


	@Test
	void findAllByAdmin_orderBy가_null이면_예외가정상발생하는지(){
		//given
		UsersOrderBy orderBy = null;

		//when & then
		assertThatThrownBy(() -> useCase.findAllByAdmin(orderBy))
			.isInstanceOf(InvalidUserOrderException.class);

	}

	@Test
	void findAllByAdmin이_orderBy에_맞게_정렬되어_조회되는지() {
		//given
		UsersOrderBy orderBy = UsersOrderBy.NAME_ASCENDING;

		var user1  = User.builder()
			.id(1L)
			.name("aaa")
			.status(Status.ACTIVE)
			.build();

		var user2  = User.builder()
			.id(2L)
			.name("bbb")
			.status(Status.WAITING)
			.build();

		var cd1 = Cardinal.builder()
			.id(1L)
			.cardinalNumber(6)
			.year(2020)
			.semester(2)
			.build();

		var cd2 = Cardinal.builder()
			.id(2L)
			.cardinalNumber(7)
			.year(2021)
			.semester(1)
			.build();

		var uc1 = new UserCardinal(user1, cd1);
		var uc2 = new UserCardinal(user2, cd2);

		var adminResponse1 = new UserResponseDto.AdminResponse(
			1, "aaa", "a@a.com", "202034420", "01011112222", "산업공학과",
			List.of(6), null, Status.ACTIVE, null,
			0, 0, 0, 0, 0,
			LocalDateTime.now().minusDays(3),
			LocalDateTime.now()
		);

		var adminResponse2 = new UserResponseDto.AdminResponse(
			2, "bbb", "b@b.com", "202045678", "01033334444", "컴퓨터공학과",
			List.of(7), null, Status.WAITING, null,
			0, 0, 0, 0, 0,
			LocalDateTime.now().minusDays(2),
			LocalDateTime.now()
		);



		given(userCardinalGetService.getUserCardinals(user1)).willReturn(List.of(uc1));
		given(userCardinalGetService.getUserCardinals(user2)).willReturn(List.of(uc2));
		given(userCardinalGetService.findAll()).willReturn(List.of(uc1, uc2));
		given(userMapper.toAdminResponse(user1, List.of(uc1))).willReturn(adminResponse1);
		given(userMapper.toAdminResponse(user2, List.of(uc2))).willReturn(adminResponse2);


		//when
		var result = useCase.findAllByAdmin(UsersOrderBy.NAME_ASCENDING);


		//then
		assertThat(result).hasSize(2);
		assertThat(result.get(0).name()).isEqualTo("aaa");
		assertThat(result.get(1).name()).isEqualTo("bbb");

	}


	@Test
	void accept_비활성유저_승인시_출석초기화_정상호출되는지() {
		//given
		var user1 = User.builder()
			.id(1L)
			.name("aaa")
			.status(Status.WAITING)
			.build();
		var userIds = new UserRequestDto.UserId(List.of(1L));
		var cardinal = Cardinal.builder()
			.id(1L)
			.cardinalNumber(8)
			.build();
		var meetings = List.of(mock(Meeting.class));

		given(userGetService.findAll(userIds.userId())).willReturn(List.of(user1));
		given(userCardinalGetService.getCurrentCardinal(user1)).willReturn(cardinal);
		given(meetingGetService.find(8)).willReturn(meetings);

		//when
		useCase.accept(userIds);

		//then
		then(userUpdateService).should().accept(user1);
		then(attendanceSaveService).should().init(user1,meetings);

	}

}
