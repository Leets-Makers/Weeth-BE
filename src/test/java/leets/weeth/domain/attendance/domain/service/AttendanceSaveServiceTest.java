package leets.weeth.domain.attendance.domain.service;

import static leets.weeth.domain.attendance.test.fixture.AttendanceTestFixture.*;
import static leets.weeth.domain.schedule.test.fixture.ScheduleTestFixture.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import leets.weeth.domain.attendance.domain.entity.Attendance;
import leets.weeth.domain.attendance.domain.repository.AttendanceRepository;
import leets.weeth.domain.schedule.domain.entity.Meeting;
import leets.weeth.domain.user.domain.entity.User;

@ExtendWith(MockitoExtension.class)
class AttendanceSaveServiceTest {

	@Mock private AttendanceRepository attendanceRepository;
	@InjectMocks private AttendanceSaveService attendanceSaveService;

	@Test
	@DisplayName("init(user, meetings): 각 미팅에 대한 Attendance 저장 후 user.add 호출")
	void init_createsAttendanceAndLinkToUser() {
		// given
		User user = mock(User.class);
		Meeting meetingFirst = createMeeting();
		Meeting meetingSecond = createMeeting();

		// save가 새로운 Attendance를 반환하는 동작을 그대로 흉내 (인자로 받은 객체를 그대로 반환)
		when(attendanceRepository.save(any(Attendance.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		// when
		attendanceSaveService.init(user, List.of(meetingFirst, meetingSecond));

		// then
		verify(attendanceRepository, times(2)).save(any(Attendance.class));
		verify(user, times(2)).add(any(Attendance.class));
	}

	@Test
	@DisplayName("saveAll(users, meeting): 사용자 수만큼 Attendance 생성 후 saveAll 호출")
	void saveAll_bulkInsert() {
		// given
		Meeting meeting = createMeeting();
		User userFirst = createActiveUser("이지훈");
		User userSecond = createActiveUser("이강혁");

		// when
		attendanceSaveService.saveAll(List.of(userFirst, userSecond), meeting);

		// then
		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<Attendance>> listCaptor = ArgumentCaptor.forClass(List.class);
		verify(attendanceRepository).saveAll(listCaptor.capture());

		List<Attendance> savedAttendances = listCaptor.getValue();
		Assertions.assertThat(savedAttendances).hasSize(2);

		Assertions.assertThat(savedAttendances)
			.allSatisfy(att -> Assertions.assertThat(att.getMeeting()).isSameAs(meeting));
		Assertions.assertThat(savedAttendances)
			.extracting(Attendance::getUser)
			.containsExactlyInAnyOrder(userFirst, userSecond);
	}
}
