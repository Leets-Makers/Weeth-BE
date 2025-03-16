package leets.weeth.domain.schedule.application.mapper;

import leets.weeth.domain.schedule.application.dto.ScheduleDTO;
import leets.weeth.domain.schedule.domain.entity.Event;
import leets.weeth.domain.user.domain.entity.User;
import org.mapstruct.*;

import static leets.weeth.domain.schedule.application.dto.EventDTO.Response;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(target = "name", source = "event.user.name")
    @Mapping(target = "type", expression = "java(Type.EVENT)")
    Response to(Event event);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", source = "user")
    })
    Event from(ScheduleDTO.Save dto, User user);
}
