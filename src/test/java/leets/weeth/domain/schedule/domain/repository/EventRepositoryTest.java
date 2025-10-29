package leets.weeth.domain.schedule.domain.repository;

import leets.weeth.config.TestContainersConfig;
import leets.weeth.domain.schedule.domain.entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestContainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        Event event1 = Event.builder()
                .title("Event 1")
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .cardinal(1)
                .build();

        Event event2 = Event.builder()
                .title("Event 2")
                .start(now.plusDays(3))
                .end(now.plusDays(4))
                .cardinal(1)
                .build();

        Event event3 = Event.builder()
                .title("Event 3")
                .start(now.plusDays(5))
                .end(now.plusDays(6))
                .cardinal(2)
                .build();

        eventRepository.saveAll(List.of(event1, event2, event3));
    }

    @Test
    void testFindByStartLessThanEqualAndEndGreaterThanEqualOrderByStartAsc() {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(7);

        // when
        List<Event> events = eventRepository.findByStartLessThanEqualAndEndGreaterThanEqualOrderByStartAsc(end, start);

        // then
        assertThat(events)
                .hasSize(2)
                .extracting(Event::getTitle)
                .containsExactly("Event 2", "Event 3"); // 순서와 구성이 모두 일치하는지
    }

    @Test
    void testFindAllByCardinal() {
        // given
        int cardinal = 1;

        // when
        List<Event> events = eventRepository.findAllByCardinal(cardinal);

        // then
        assertThat(events)
                .hasSize(2)
                .extracting(Event::getTitle)
                .containsExactly("Event 1", "Event 2");
    }
}
