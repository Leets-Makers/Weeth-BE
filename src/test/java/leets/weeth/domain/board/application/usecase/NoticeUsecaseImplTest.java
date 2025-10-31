package leets.weeth.domain.board.application.usecase;

import leets.weeth.domain.board.application.dto.NoticeDTO;
import leets.weeth.domain.board.application.mapper.NoticeMapper;
import leets.weeth.domain.board.domain.entity.Notice;
import leets.weeth.domain.board.domain.fixture.NoticeFixture;
import leets.weeth.domain.board.domain.service.NoticeFindService;
import leets.weeth.domain.file.domain.entity.File;
import leets.weeth.domain.file.domain.service.FileGetService;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.entity.enums.Department;
import leets.weeth.domain.user.domain.entity.enums.Position;
import leets.weeth.domain.user.domain.entity.enums.Role;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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

        given(noticeFindService.findRecentNotices(any(Pageable.class))).willReturn(slice);
        given(fileGetService.findAllByNotice(any())).willReturn(List.of());

        given(noticeMapper.toAll(any(Notice.class), anyBoolean()))
                .willAnswer(invocation -> {
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
        assertThat(noticeResponses.hasNext()).isTrue();

        verify(noticeFindService, times(1)).findRecentNotices(pageable);

    }

    @Test
    void 공지사항_검색시_결과와_파일_존재여부가_정상적으로_반환() {
        // given
        User user = User.builder()
                .email("abc@test.com")
                .name("홍길동")
                .position(Position.BE)
                .department(Department.SW)
                .role(Role.USER)
                .build();

        List<Notice> notices = new ArrayList<>();
        for(int i = 0; i<3; i++){
            Notice notice = NoticeFixture.createNotice("공지" + i, user);
            ReflectionTestUtils.setField(notice, "id", (long) i + 1);
            notices.add(notice);
        }
        for(int i = 3; i<6; i++){
            Notice notice = NoticeFixture.createNotice("검색" + i, user);
            ReflectionTestUtils.setField(notice, "id", (long) i + 1);
            notices.add(notice);
        }


        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));

        Slice<Notice> slice = new SliceImpl<>(List.of(notices.get(5), notices.get(4), notices.get(3)), pageable, false);

        given(noticeFindService.search(any(String.class), any(Pageable.class))).willReturn(slice);
        // 짝수 id - 파일 존재, 홀수 id - 파일 없음 (빈 리스트)
        given(fileGetService.findAllByNotice(any()))
                .willAnswer(invocation -> {
                    Long noticeId = invocation.getArgument(0);
                    if (noticeId % 2 == 0) {
                        return List.of(File.builder()
                                .notice(notices.get((int)(noticeId-1)))
                                .build());
                    } else {
                        return List.of();
                    }
                });

        given(noticeMapper.toAll(any(Notice.class), anyBoolean()))
                .willAnswer(invocation -> {
                    Notice notice = invocation.getArgument(0);
                    boolean fileExists = invocation.getArgument(1);
                    return new NoticeDTO.ResponseAll(
                            notice.getId(),
                            notice.getUser() != null ? notice.getUser().getName() : "",
                            notice.getUser() != null ? notice.getUser().getPosition() : Position.BE,
                            notice.getUser() != null ? notice.getUser().getRole() : Role.USER,
                            notice.getTitle(),
                            notice.getContent(),
                            notice.getCreatedAt(),
                            notice.getCommentCount(),
                            fileExists
                    );
                });

        // when
        Slice<NoticeDTO.ResponseAll> noticeResponses = noticeUsecase.searchNotice("검색", 0, 5);

        // then
        assertThat(noticeResponses).isNotNull();
        assertThat(noticeResponses.getContent()).hasSize(3);
        assertThat(noticeResponses.getContent().get(0).title()).isEqualTo(notices.get(5).getTitle());
        assertThat(noticeResponses.hasNext()).isFalse();
        
        // 짝수 id : 파일 존재, 홀수 id : 파일 없음 검증
        assertThat(noticeResponses.getContent().get(0).hasFile()).isTrue();
        assertThat(noticeResponses.getContent().get(1).hasFile()).isFalse();

        verify(noticeFindService, times(1)).search("검색", pageable);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
