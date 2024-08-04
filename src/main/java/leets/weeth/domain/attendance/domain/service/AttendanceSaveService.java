package leets.weeth.domain.attendance.domain.service;

import jakarta.transaction.Transactional;
import leets.weeth.domain.attendance.domain.entity.Attendance;
import leets.weeth.domain.attendance.domain.repository.AttendanceRepository;
import leets.weeth.domain.schedule.domain.entity.Meeting;
import leets.weeth.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceSaveService {

    private final AttendanceRepository attendanceRepository;

    @Transactional
    public void save(User user, List<Meeting> meetings) {
        meetings.forEach(meeting -> {
            Attendance attendance = attendanceRepository.save(new Attendance(meeting, user));
            user.add(attendance);
        });
    }
}
