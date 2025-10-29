package leets.weeth.domain.schedule.domain.service;

import leets.weeth.domain.schedule.application.exception.EventNotFoundException;
import leets.weeth.domain.schedule.application.mapper.ScheduleMapper;
import leets.weeth.domain.schedule.domain.entity.Event;
import leets.weeth.domain.schedule.domain.repository.EventRepository;
import leets.weeth.domain.schedule.test.fixture.ScheduleTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventGetServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private EventGetService eventGetService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = ScheduleTestFixture.createEvent();
    }

    @Test
    void testFind() {
        // given
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(testEvent));

        // when
        Event event = eventGetService.find(1L);

        // then
        assertThat(event).isEqualTo(testEvent);
        verify(eventRepository).findById(1L);
    }

    @Test
    void testFindNotFound() {
        // given
        Long id = 999L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        // 이벤트가 존재하지 않을 때 예외가 발생하는지 확인
        assertThatThrownBy(() -> eventGetService.find(id))
                .isInstanceOf(EventNotFoundException.class);
        verify(eventRepository).findById(id);
    }
}
