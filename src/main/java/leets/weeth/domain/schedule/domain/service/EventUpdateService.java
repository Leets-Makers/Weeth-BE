package leets.weeth.domain.schedule.domain.service;

import jakarta.transaction.Transactional;
import leets.weeth.domain.schedule.domain.entity.Event;
import leets.weeth.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static leets.weeth.domain.schedule.application.dto.EventDTO.Update;

@Service
@Transactional
@RequiredArgsConstructor
public class EventUpdateService {

    public void update(Event event, Update dto, User user) {
        event.update(dto, user);
    }
}
