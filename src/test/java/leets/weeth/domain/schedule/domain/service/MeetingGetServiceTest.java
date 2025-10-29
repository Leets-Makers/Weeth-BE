package leets.weeth.domain.schedule.domain.service;

import leets.weeth.domain.schedule.application.exception.MeetingNotFoundException;
import leets.weeth.domain.schedule.application.mapper.ScheduleMapper;
import leets.weeth.domain.schedule.domain.entity.Meeting;
import leets.weeth.domain.schedule.domain.repository.MeetingRepository;
import leets.weeth.domain.schedule.test.fixture.ScheduleTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class MeetingGetServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private ScheduleMapper scheduleMapper;

    @InjectMocks
    private MeetingGetService meetingGetService;

    private Meeting testMeeting;

    @BeforeEach
    void setUp() {
        testMeeting = ScheduleTestFixture.createMeeting();
    }

    @Test
    @DisplayName("[MeetingGetService] 기본 조회 메서드 테스트")
    void testFind() {
        // given
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.of(testMeeting));


        // when
        Meeting meeting = meetingGetService.find(1L);


        // when
        assertThat(meeting).isEqualTo(testMeeting);
        verify(meetingRepository).findById(1L);
    }

    @Test
    @DisplayName("[MeetingGetService] null인 경우 예외 발생 테스트")
    void testFindNotFound() {
        // given
        when(meetingRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> meetingGetService.find(1L))
            .isInstanceOf(MeetingNotFoundException.class);
        verify(meetingRepository).findById(1L);
    }
}
