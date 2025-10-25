package leets.weeth.domain.board.application.usecase;

import leets.weeth.domain.board.application.dto.NoticeDTO;
import leets.weeth.domain.board.application.mapper.NoticeMapper;
import leets.weeth.domain.board.domain.entity.Notice;
import leets.weeth.domain.board.domain.fixture.NoticeFixture;
import leets.weeth.domain.board.domain.service.NoticeFindService;
import leets.weeth.domain.file.domain.service.FileGetService;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.entity.enums.Department;
import leets.weeth.domain.user.domain.entity.enums.Position;
import leets.weeth.domain.user.domain.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeUsecaseImplTest {

    @Mock
    private NoticeFindService noticeFindService;
    @Mock
    private FileGetService fileGetService;

    @InjectMocks
    private NoticeUsecaseImpl noticeUsecase;

    @Mock
    private NoticeMapper noticeMapper;

    @Test
    void 공지사항이_최신순으로_정렬되는지() {
        // given
        User user = User.builder()
                .email("abc@test.com")
                .name("홍길동")
                .position(Position.BE)
                .department(Department.SW)
                .role(Role.USER)
                .build();

        List<Notice> notices = new ArrayList<>();
        for(int i = 0; i<5; i++){
            Notice notice = NoticeFixture.createNotice("공지" + i, user);
            ReflectionTestUtils.setField(notice, "id", (long) i + 1);
            notices.add(notice);
        }

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"));

        Slice<Notice> slice = new SliceImpl<>(List.of(notices.get(4), notices.get(3), notices.get(2)), pageable, true);

        when(noticeFindService.findRecentNotices(any(Pageable.class))).thenReturn(slice);
        when(fileGetService.findAllByNotice(nullable(Long.class))).thenReturn(List.of());

        when(noticeMapper.toAll(any(Notice.class), anyBoolean()))
                .thenAnswer(invocation -> {
                    Notice notice = invocation.getArgument(0);
                    return new NoticeDTO.ResponseAll(
                            notice.getId(),
                            notice.getUser() != null ? notice.getUser().getName() : "",
                            notice.getUser() != null ? notice.getUser().getPosition() : Position.BE,
                            notice.getUser() != null ? notice.getUser().getRole() : Role.USER,
                            notice.getTitle(),
                            notice.getContent(),
                            notice.getCreatedAt(),
                            notice.getCommentCount(),
                            false
                    );
                });

        // when
        Slice<NoticeDTO.ResponseAll> noticeResponses = noticeUsecase.findNotices(0, 3);

        // then
        assertThat(noticeResponses).isNotNull();
        assertThat(noticeResponses.getContent()).hasSize(3);
        assertThat(noticeResponses.getContent().get(0).title()).isEqualTo(notices.get(4).getTitle());

        verify(noticeFindService, times(1)).findRecentNotices(pageable);

    }

    @Test
    void searchNotice() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
