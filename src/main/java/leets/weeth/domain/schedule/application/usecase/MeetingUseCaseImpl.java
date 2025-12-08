package leets.weeth.domain.schedule.application.usecase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import leets.weeth.domain.attendance.domain.entity.Attendance;
import leets.weeth.domain.attendance.domain.service.AttendanceDeleteService;
import leets.weeth.domain.attendance.domain.service.AttendanceGetService;
import leets.weeth.domain.attendance.domain.service.AttendanceSaveService;
import leets.weeth.domain.attendance.domain.service.AttendanceUpdateService;
import leets.weeth.domain.schedule.application.dto.ScheduleDTO;
import leets.weeth.domain.schedule.application.mapper.MeetingMapper;
import leets.weeth.domain.schedule.domain.entity.Meeting;
import leets.weeth.domain.schedule.domain.service.MeetingDeleteService;
import leets.weeth.domain.schedule.domain.service.MeetingGetService;
import leets.weeth.domain.schedule.domain.service.MeetingSaveService;
import leets.weeth.domain.schedule.domain.service.MeetingUpdateService;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.entity.enums.Role;
import leets.weeth.domain.user.domain.service.CardinalGetService;
import leets.weeth.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static leets.weeth.domain.schedule.application.dto.MeetingDTO.Info;
import static leets.weeth.domain.schedule.application.dto.MeetingDTO.Response;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingUseCaseImpl implements MeetingUseCase {

    private final MeetingGetService meetingGetService;
    private final MeetingMapper mapper;
    private final MeetingSaveService meetingSaveService;
    private final UserGetService userGetService;
    private final MeetingUpdateService meetingUpdateService;
    private final MeetingDeleteService meetingDeleteService;
    private final AttendanceGetService attendanceGetService;
    private final AttendanceSaveService attendanceSaveService;
    private final AttendanceDeleteService attendanceDeleteService;
    private final AttendanceUpdateService attendanceUpdateService;
    private final CardinalGetService cardinalGetService;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Response find(Long userId, Long meetingId) {
        User user = userGetService.find(userId);
        Meeting meeting = meetingGetService.find(meetingId);

        if (Role.ADMIN == user.getRole()) {
            return mapper.toAdminResponse(meeting)  ;
        }

        return mapper.to(meeting);
    }

    @Override
    public List<Info> find(Integer cardinal) {
        List<Meeting> meetings;
        List<Info> result = new ArrayList<>();

        if (cardinal == null) {
            meetings = meetingGetService.findAll();

        } else {
            meetings = meetingGetService.findMeetingByCardinal(cardinal);

        }

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Meeting thisWeek = meetings.stream()
            .filter(m -> {
                LocalDate d = m.getStart().toLocalDate();
                return !d.isBefore(startOfWeek) && !d.isAfter(endOfWeek);
            })
            .findFirst()
            .orElse(null);

        if (thisWeek != null) {
            result.add(mapper.toInfo(thisWeek));
        }

        result.addAll(
            meetings.stream()
                .sorted(Comparator.comparing(Meeting::getStart).reversed())
                .map(mapper::toInfo)
                .toList()
        );

        return result;
    }

    @Override
    @Transactional
    public void save(ScheduleDTO.Save dto, Long userId) {
        User user = userGetService.find(userId);
        Cardinal cardinal = cardinalGetService.findByUserSide(dto.cardinal());

        List<User> userList = userGetService.findAllByCardinal(cardinal);

        Meeting meeting = mapper.from(dto, user);
        meetingSaveService.save(meeting);

        attendanceSaveService.saveAll(userList, meeting);
    }

    @Override
    @Transactional
    public void update(ScheduleDTO.Update dto, Long userId, Long meetingId) {
        Meeting meeting = meetingGetService.find(meetingId);
        User user = userGetService.find(userId);
        meetingUpdateService.update(dto, user, meeting);
    }

    @Override
    @Transactional
    public void delete(Long meetingId) {
        Meeting meeting = meetingGetService.find(meetingId);
        List<Attendance> attendances = attendanceGetService.findAllByMeeting(meeting);

        attendanceUpdateService.updateUserAttendanceByStatus(attendances);

        em.flush();
        em.clear();

        attendanceDeleteService.deleteAll(meeting);
        meetingDeleteService.delete(meeting);
    }
}
