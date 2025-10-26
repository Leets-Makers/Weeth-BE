package leets.weeth.domain.attendance.application;

import static leets.weeth.domain.attendance.test.fixture.AttendanceTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import leets.weeth.domain.attendance.application.dto.AttendanceDTO;
import leets.weeth.domain.attendance.application.exception.AttendanceCodeMismatchException;
import leets.weeth.domain.attendance.application.exception.AttendanceNotFoundException;
import leets.weeth.domain.attendance.application.mapper.AttendanceMapper;
import leets.weeth.domain.attendance.application.usecase.AttendanceUseCaseImpl;
import leets.weeth.domain.attendance.domain.entity.Attendance;
import leets.weeth.domain.attendance.domain.entity.enums.Status;
import leets.weeth.domain.attendance.domain.service.AttendanceGetService;
import leets.weeth.domain.attendance.domain.service.AttendanceUpdateService;
import leets.weeth.domain.schedule.application.exception.MeetingNotFoundException;
import leets.weeth.domain.schedule.domain.entity.Meeting;
import leets.weeth.domain.schedule.domain.service.MeetingGetService;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.service.UserCardinalGetService;
import leets.weeth.domain.user.domain.service.UserGetService;

@ExtendWith(MockitoExtension.class)
public class AttendanceUseCaseImplTest {

	private final Long userId = 10L;
	@Mock private UserGetService userGetService;
	@Mock private UserCardinalGetService userCardinalGetService;
	@Mock private AttendanceGetService attendanceGetService;
	@Mock private AttendanceUpdateService attendanceUpdateService;
	@Mock private AttendanceMapper attendanceMapper;
	@Mock private MeetingGetService meetingGetService;
	@InjectMocks private AttendanceUseCaseImpl attendanceUseCase;

	@Test
	@DisplayName("find: 오늘 날짜의 정기모임이 있으면 해당 정기모임으로 Main 매핑")
	void find_todayMeeting() {
		// given
		LocalDate today = LocalDate.now();
		Meeting todayMeeting = createOneDayMeeting(today, 1, 1111, "Today Meeting");
		User user = createActiveUserWithAttendances("이지훈", List.of(todayMeeting));
		Attendance todayAttendance = user.getAttendances().get(0);

		when(userGetService.find(userId)).thenReturn(user);
		when(attendanceMapper.toMainDto(eq(user), eq(todayAttendance)))
			.thenReturn(mock(AttendanceDTO.Main.class));

		// when
		AttendanceDTO.Main actual = attendanceUseCase.find(userId);

		// then
		assertThat(actual).isNotNull();
		verify(attendanceMapper).toMainDto(eq(user), eq(todayAttendance));
	}

	@Test
	@DisplayName("findAllDetailsByCurrentCardinal: 현재 기수만 필터링·정렬하여 Detail 매핑")
	void findAllDetailsByCurrentCardinal() {
		// given
		LocalDate today = LocalDate.now();
		Meeting meetingDayMinus1 = createOneDayMeeting(today.minusDays(1), 1, 1111, "D-1");
		Meeting meetingToday = createOneDayMeeting(today, 1, 2222, "D-Day");
		User user = createActiveUserWithAttendances("이지훈", List.of(meetingDayMinus1, meetingToday));

		List<Attendance> userAttendances = user.getAttendances();
		Attendance attendanceFirst = userAttendances.get(0);   // D-1
		Attendance attendanceSecond = userAttendances.get(1);  // D-Day

		when(userGetService.find(userId)).thenReturn(user);
		Cardinal currentCardinal = mock(Cardinal.class);
		when(currentCardinal.getCardinalNumber()).thenReturn(1);
		when(userCardinalGetService.getCurrentCardinal(user)).thenReturn(currentCardinal);

		AttendanceDTO.Response responseFirst = mock(AttendanceDTO.Response.class);
		AttendanceDTO.Response responseSecond = mock(AttendanceDTO.Response.class);
		when(attendanceMapper.toResponseDto(attendanceFirst)).thenReturn(responseFirst);
		when(attendanceMapper.toResponseDto(attendanceSecond)).thenReturn(responseSecond);

		AttendanceDTO.Detail expectedDetail = mock(AttendanceDTO.Detail.class);
		when(attendanceMapper.toDetailDto(eq(user), anyList())).thenReturn(expectedDetail);

		// when
		AttendanceDTO.Detail actualDetail = attendanceUseCase.findAllDetailsByCurrentCardinal(userId);

		// then
		assertThat(actualDetail).isSameAs(expectedDetail);
		verify(attendanceMapper).toDetailDto(eq(user), argThat(list -> list.size() == 2));
	}

	@Test
	@DisplayName("findAllAttendanceByMeeting: 정기모임 조회 후 해당 출석들을 DTO로 매핑")
	void findAllAttendanceByMeeting() {
		// given
		Meeting meeting = createOneDayMeeting(LocalDate.now(), 1, 1111, "Today");
		Attendance a1 = createAttendance(meeting, createActiveUser("A"));
		Attendance a2 = createAttendance(meeting, createActiveUser("B"));

		when(meetingGetService.find(99L)).thenReturn(meeting);
		when(attendanceGetService.findAllByMeeting(meeting)).thenReturn(List.of(a1, a2));
		AttendanceDTO.AttendanceInfo i1 = mock(AttendanceDTO.AttendanceInfo.class);
		AttendanceDTO.AttendanceInfo i2 = mock(AttendanceDTO.AttendanceInfo.class);
		when(attendanceMapper.toAttendanceInfoDto(a1)).thenReturn(i1);
		when(attendanceMapper.toAttendanceInfoDto(a2)).thenReturn(i2);

		// when
		List<AttendanceDTO.AttendanceInfo> actual = attendanceUseCase.findAllAttendanceByMeeting(99L);

		// then
		assertThat(actual).containsExactly(i1, i2);
	}

	@Test
	@DisplayName("close(now, cardinal): 당일 정기모임을 찾아 close")
	void close_success() {
		// given
		LocalDate now = LocalDate.now();
		Meeting targetMeeting = createOneDayMeeting(now, 1, 1111, "Today");
		Meeting otherMeeting  = createOneDayMeeting(now.minusDays(1), 1, 9999, "Yesterday");

		Attendance attendance1 = mock(Attendance.class);
		Attendance attendance2 = mock(Attendance.class);

		when(meetingGetService.find(1)).thenReturn(List.of(targetMeeting, otherMeeting));
		when(attendanceGetService.findAllByMeeting(targetMeeting)).thenReturn(List.of(attendance1, attendance2));

		// when
		attendanceUseCase.close(now, 1);

		// then
		verify(attendanceUpdateService).close(argThat(list ->
			list.size() == 2 && list.containsAll(List.of(attendance1, attendance2))
		));
	}

	@Test
	@DisplayName("close(now, cardinal): 당일 정기모임이 없으면 MeetingNotFoundException")
	void close_notFound() {
		// given
		LocalDate now = LocalDate.now();
		Meeting otherDayMeeting = createOneDayMeeting(now.minusDays(1), 1, 9999, "Yesterday");

		when(meetingGetService.find(1)).thenReturn(List.of(otherDayMeeting));

		// when & then
		assertThatThrownBy(() -> attendanceUseCase.close(now, 1))
			.isInstanceOf(MeetingNotFoundException.class);
	}

	@Nested
	@DisplayName("checkIn")
	class CheckInTest {

		@Test
		@DisplayName("진행 중 정기모임이고 코드 일치하며 상태가 ATTEND가 아니면 출석 처리")
		void checkIn_success() {
			// given
			User user = mock(User.class);
			Meeting inProgressMeeting = createInProgressMeeting(1, 1234, "InProgress");
			Attendance attendance = mock(Attendance.class);
			when(attendance.getMeeting()).thenReturn(inProgressMeeting);
			when(attendance.isWrong(1234)).thenReturn(false);
			when(attendance.getStatus()).thenReturn(Status.PENDING);

			when(userGetService.find(userId)).thenReturn(user);
			when(user.getAttendances()).thenReturn(List.of(attendance));

			// when
			attendanceUseCase.checkIn(userId, 1234);

			// then
			verify(attendanceUpdateService).attend(attendance);
		}

		@Test
		@DisplayName("진행 중 정기모임이 없으면 AttendanceNotFoundException")
		void checkIn_notFoundMeeting() {
			// given
			User user = mock(User.class);
			when(userGetService.find(userId)).thenReturn(user);
			when(user.getAttendances()).thenReturn(List.of());

			// when & then
			assertThatThrownBy(() -> attendanceUseCase.checkIn(userId, 1234))
				.isInstanceOf(AttendanceNotFoundException.class);
		}

		@Test
		@DisplayName("코드 불일치 시 AttendanceCodeMismatchException")
		void checkIn_wrongCode() {
			// given
			User user = mock(User.class);
			Meeting inProgressMeeting = createInProgressMeeting(1, 1234, "InProgress");

			Attendance attendance = mock(Attendance.class);
			when(attendance.getMeeting()).thenReturn(inProgressMeeting);
			when(attendance.isWrong(9999)).thenReturn(true);

			when(userGetService.find(userId)).thenReturn(user);
			when(user.getAttendances()).thenReturn(List.of(attendance));

			// when & then
			assertThatThrownBy(() -> attendanceUseCase.checkIn(userId, 9999))
				.isInstanceOf(AttendanceCodeMismatchException.class);
		}

		@Test
		@DisplayName("이미 ATTEND면 추가 처리 없이 종료")
		void checkIn_alreadyAttend() {
			// given
			User user = mock(User.class);
			Meeting inProgressMeeting = createInProgressMeeting(1, 1234, "InProgress");

			Attendance attendance = mock(Attendance.class);
			when(attendance.getMeeting()).thenReturn(inProgressMeeting);
			when(attendance.isWrong(1234)).thenReturn(false);
			when(attendance.getStatus()).thenReturn(Status.ATTEND);

			when(userGetService.find(userId)).thenReturn(user);
			when(user.getAttendances()).thenReturn(List.of(attendance));

			// when
			attendanceUseCase.checkIn(userId, 1234);

			// then
			verify(attendanceUpdateService, never()).attend(any());
		}
	}
}
