package leets.weeth.domain.attendance.test.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import leets.weeth.domain.attendance.domain.entity.Attendance;
import leets.weeth.domain.schedule.domain.entity.Meeting;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.entity.enums.Status;

public class AttendanceTestFixture {

	private AttendanceTestFixture() {}

	//todo : 추후 User Fixture 활용 예정
	public static User createActiveUser(String name) {
		return User.builder().name(name).status(Status.ACTIVE).build();
	}

	//todo : 추후 User Fixture 활용 예정
	public static User createActiveUserWithAttendances(String name, List<Meeting> meetings) {
		User user = createActiveUser(name);

		if (user.getAttendances() == null) {
			try {
				java.lang.reflect.Field f = user.getClass().getDeclaredField("attendances");
				f.setAccessible(true);
				f.set(user, new java.util.ArrayList<>());
			} catch (Exception ignore) {}
		}
		if (meetings != null) {
			for (Meeting meeting : meetings) {
				Attendance attendance = createAttendance(meeting, user);
				user.add(attendance);
			}
		}
		return user;
	}

	public static Attendance createAttendance(Meeting meeting, User user) {
		return new Attendance(meeting, user);
	}

	public static Meeting createOneDayMeeting(LocalDate date, int cardinal, int code, String title) {
		return Meeting.builder()
			.title(title)
			.location("Test Location")
			.start(date.atTime(10, 0))
			.end(date.atTime(12, 0))
			.code(code)
			.cardinal(cardinal)
			.build();
	}

	public static Meeting createInProgressMeeting(int cardinal, int code, String title) {
		return Meeting.builder()
			.title(title)
			.location("Test Location")
			.start(LocalDateTime.now().minusMinutes(5))
			.end(LocalDateTime.now().plusMinutes(5))
			.code(code)
			.cardinal(cardinal)
			.build();
	}
}
