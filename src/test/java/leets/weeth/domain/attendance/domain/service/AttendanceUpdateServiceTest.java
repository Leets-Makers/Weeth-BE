package leets.weeth.domain.attendance.domain.service;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import leets.weeth.domain.attendance.domain.entity.Attendance;
import leets.weeth.domain.attendance.domain.entity.enums.Status;
import leets.weeth.domain.user.domain.entity.User;

public class AttendanceUpdateServiceTest {

	private final AttendanceUpdateService attendanceUpdateService = new AttendanceUpdateService();

	@Test
	@DisplayName("attend(): attendance.attend() + user.attend() 호출")
	void attend_callsEntityMethods() {
		// given
		Attendance attendance = mock(Attendance.class);
		User user = mock(User.class);
		when(attendance.getUser()).thenReturn(user);

		// when
		attendanceUpdateService.attend(attendance);

		// then
		verify(attendance).attend();
		verify(user).attend();
	}

	@Test
	@DisplayName("close(): pending만 close() + user.absent() 호출")
	void close_onlyPendingIsClosed() {
		// given
		Attendance pending = mock(Attendance.class);
		Attendance nonPending = mock(Attendance.class);
		User pendingUser = mock(User.class);
		User nonPendingUser = mock(User.class);

		when(pending.isPending()).thenReturn(true);
		when(nonPending.isPending()).thenReturn(false);
		when(pending.getUser()).thenReturn(pendingUser);
		when(nonPending.getUser()).thenReturn(nonPendingUser);

		// when
		attendanceUpdateService.close(List.of(pending, nonPending));

		// then
		verify(pending).close();
		verify(pendingUser).absent();
		verify(nonPending, never()).close();
		verify(nonPendingUser, never()).absent();
	}

	@Test
	@DisplayName("updateUserAttendanceByStatus가 ATTEND면 user.removeAttend(), 그 외에는 user.removeAbsent() 처리")
	void updateUserAttendanceByStatus() {
		// given
		Attendance attend = mock(Attendance.class);
		Attendance absent = mock(Attendance.class);
		User userA = mock(User.class);
		User userB = mock(User.class);

		when(attend.getStatus()).thenReturn(Status.ATTEND);
		when(absent.getStatus()).thenReturn(Status.ABSENT);
		when(attend.getUser()).thenReturn(userA);
		when(absent.getUser()).thenReturn(userB);

		// when
		attendanceUpdateService.updateUserAttendanceByStatus(List.of(attend, absent));

		// then
		verify(userA).removeAttend();
		verify(userB).removeAbsent();
	}
}
