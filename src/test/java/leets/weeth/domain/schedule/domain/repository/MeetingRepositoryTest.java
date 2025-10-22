package leets.weeth.domain.schedule.domain.repository;

import leets.weeth.config.TestContainersConfig;
import leets.weeth.domain.schedule.domain.entity.Meeting;
import leets.weeth.domain.schedule.domain.entity.enums.MeetingStatus;
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
class MeetingRepositoryTest {
    @Autowired
    private MeetingRepository meetingRepository;

    @BeforeEach
    void setUp() {
        Meeting meeting1 = Meeting.builder()
                .title("Meeting 1")
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .code(1111)
                .cardinal(1)
                .meetingStatus(MeetingStatus.OPEN)
                .build();

        Meeting meeting2 = Meeting.builder()
                .title("Meeting 2")
                .start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(4))
                .code(2222)
                .cardinal(1)
                .meetingStatus(MeetingStatus.OPEN)
                .build();

        Meeting meeting3 = Meeting.builder()
                .title("Meeting 3")
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(6))
                .code(3333)
                .cardinal(2)
                .meetingStatus(MeetingStatus.CLOSE)
                .build();

        meetingRepository.saveAll(java.util.List.of(meeting1, meeting2, meeting3));
    }


    @Test
    void findByStartLessThanEqualAndEndGreaterThanEqualOrderByStartAsc() {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(4);

        // when
        List<Meeting> meetings = meetingRepository.findByStartLessThanEqualAndEndGreaterThanEqualOrderByStartAsc(end, start);

        // then
        assertThat(meetings)
                .hasSize(2)
                .extracting(Meeting::getTitle)
                .containsExactly("Meeting 1", "Meeting 2");
    }

    @Test
    void findAllByCardinalOrderByStartAsc() {
        // given
        int cardinal = 1;

        // when
        List<Meeting> meetings = meetingRepository.findAllByCardinalOrderByStartAsc(cardinal);

        // then
        assertThat(meetings)
                .hasSize(2)
                .extracting(Meeting::getTitle)
                .containsExactly("Meeting 1", "Meeting 2");
    }

    @Test
    void findAllByCardinalOrderByStartDesc() {
        // given
        int cardinal = 1;

        // when
        List<Meeting> meetings = meetingRepository.findAllByCardinalOrderByStartDesc(cardinal);

        // then
        assertThat(meetings)
                .hasSize(2)
                .extracting(Meeting::getTitle)
                .containsExactly("Meeting 2", "Meeting 1");
    }

    @Test
    void findAllByCardinal() {
        // given
        int cardinal = 1;

        // when
        List<Meeting> meetings = meetingRepository.findAllByCardinal(cardinal);

        // then
        assertThat(meetings)
                .hasSize(2)
                .extracting(Meeting::getTitle)
                .containsExactlyInAnyOrder("Meeting 1", "Meeting 2");
    }

    @Test
    void findAllByMeetingStatusAndEndBeforeOrderByEndAsc() {
        // given
        MeetingStatus status = MeetingStatus.OPEN;

        // when
        List<Meeting> meetings = meetingRepository.findAllByMeetingStatusAndEndBeforeOrderByEndAsc(status, LocalDateTime.now().plusDays(5));

        // then
        assertThat(meetings)
                .hasSize(2)
                .extracting(Meeting::getTitle)
                .containsExactly("Meeting 1", "Meeting 2");

        assertThat(meetings)
                .extracting(Meeting::getMeetingStatus)
                .containsOnly(MeetingStatus.OPEN)
                .doesNotContain(MeetingStatus.CLOSE);
    }

    @Test
    void findAllByOrderByStartDesc() {
        // when
        List<Meeting> meetings = meetingRepository.findAllByOrderByStartDesc();

        // then
        assertThat(meetings)
                .hasSize(3)
                .extracting(Meeting::getTitle)
                .containsExactly("Meeting 3", "Meeting 2", "Meeting 1");
    }
}
