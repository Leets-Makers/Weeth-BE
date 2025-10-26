package leets.weeth.domain.attendance.domain.service;

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
public class AttendanceSaveServiceTest {

	@Mock private AttendanceRepository attendanceRepository;
	@InjectMocks private AttendanceSaveService attendanceSaveService;

	@Test
	@DisplayName("init(user, meetings): 각 미팅에 대한 Attendance 저장 후 user.add 호출")
	void init_createsAttendanceAndLinkToUser() {
		// given
		User user = mock(User.class);
		Meeting m1 = mock(Meeting.class);
		Meeting m2 = mock(Meeting.class);

		// when
		// save 시 반환될 Attendance를 목으로 대체
		when(attendanceRepository.save(any(Attendance.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		attendanceSaveService.init(user, List.of(m1, m2));

		// then
		// 저장은 2회, user.add는 2회
		verify(attendanceRepository, times(2)).save(any(Attendance.class));
		verify(user, times(2)).add(any(Attendance.class));
	}

	@Test
	@DisplayName("saveAll(users, meeting): 사용자 수만큼 Attendance 생성 후 saveAll 호출")
	void saveAll_bulkInsert() {
		// given
		User u1 = mock(User.class);
		User u2 = mock(User.class);
		Meeting meeting = mock(Meeting.class);

		// when
		attendanceSaveService.saveAll(List.of(u1, u2), meeting);

		// then
		ArgumentCaptor<List<Attendance>> captor = ArgumentCaptor.forClass(List.class);
		verify(attendanceRepository).saveAll(captor.capture());

		List<Attendance> saved = captor.getValue();
		Assertions.assertThat(saved).hasSize(2);
	}
}
