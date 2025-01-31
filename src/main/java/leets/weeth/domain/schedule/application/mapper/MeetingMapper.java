package leets.weeth.domain.schedule.application.mapper;

import leets.weeth.domain.schedule.application.dto.ScheduleDTO;
import leets.weeth.domain.schedule.domain.entity.Meeting;
import leets.weeth.domain.user.domain.entity.User;
import org.mapstruct.*;

import java.util.Random;

import static leets.weeth.domain.schedule.application.dto.MeetingDTO.Response;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MeetingMapper {

    @Mapping(target = "requiredItem", expression = "java(\"노트북\")")
    @Mapping(target = "name", source = "user.name")
    Response to(Meeting meeting);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "code", expression = "java( generateCode() )"),
            @Mapping(target = "user", source = "user")
    })
    Meeting from(ScheduleDTO.Save dto, User user);

    default Integer generateCode() {
        return new Random().nextInt(9000) + 1000;
    }

    /*
    차후 필히 리팩토링 할 것
    -> 정기 모임의 참여하는 인원의 멤버수를 어떻게 관리할지.
    해당 코드는 일시적인 대안책임
     */
//    default Integer getMemberCount(Meeting meeting) {
//        return (int)meeting.getAttendances().stream()
//                .filter(attendance -> !attendance.getUser().getStatus().equals(Status.BANNED))
//                .filter(attendance -> !attendance.getUser().getStatus().equals(Status.LEFT))
//                .count();
//    }
}
