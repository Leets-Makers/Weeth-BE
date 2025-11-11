package leets.weeth.domain.comment.application.usecase;

import leets.weeth.domain.board.domain.entity.Notice;
import leets.weeth.domain.board.domain.service.NoticeFindService;
import leets.weeth.domain.board.test.fixture.NoticeTestFixture;
import leets.weeth.domain.comment.application.dto.CommentDTO;
import leets.weeth.domain.comment.application.mapper.CommentMapper;
import leets.weeth.domain.comment.domain.entity.Comment;
import leets.weeth.domain.comment.domain.service.CommentDeleteService;
import leets.weeth.domain.comment.domain.service.CommentFindService;
import leets.weeth.domain.comment.domain.service.CommentSaveService;
import leets.weeth.domain.comment.test.fixture.CommentTestFixture;
import leets.weeth.domain.file.application.mapper.FileMapper;
import leets.weeth.domain.file.domain.service.FileDeleteService;
import leets.weeth.domain.file.domain.service.FileGetService;
import leets.weeth.domain.file.domain.service.FileSaveService;
import leets.weeth.domain.user.application.exception.UserNotMatchException;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.service.UserGetService;
import leets.weeth.domain.user.test.fixture.UserTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class NoticeCommentUsecaseImplTest {
    @InjectMocks NoticeCommentUsecaseImpl noticeCommentUsecase;

    @Mock CommentSaveService commentSaveService;
    @Mock CommentFindService commentFindService;
    @Mock CommentDeleteService commentDeleteService;

    @Mock FileSaveService fileSaveService;
    @Mock FileGetService fileGetService;
    @Mock FileDeleteService fileDeleteService;
    @Mock FileMapper fileMapper;

    @Mock NoticeFindService noticeFindService;

    @Mock UserGetService userGetService;
    @Mock CommentMapper commentMapper;

    @Test
    @DisplayName("부모 댓글이 없는 공지사항 댓글 작성")
    void saveNoticeComment() {
        Long userId = 1L;
        Long noticeId = 1L;
        Long commentId = 1L;
        // given
        User user  = UserTestFixture.createActiveUser1(1L);
        Notice notice = NoticeTestFixture.createNotice(noticeId, "제목1");

        CommentDTO.Save dto = new CommentDTO.Save(null, "댓글1", List.of());

        Comment comment = CommentTestFixture.createComment(commentId, dto.content(), user, notice);

        given(commentMapper.fromCommentDto(dto, notice, user, null)).willReturn(comment);
        given(userGetService.find(user.getId())).willReturn(user);
        given(noticeFindService.find(notice.getId())).willReturn(notice);
        given(fileMapper.toFileList(dto.files(), comment)).willReturn(List.of());

        // when
        noticeCommentUsecase.saveNoticeComment(dto, noticeId, userId);

        // then
        verify(userGetService).find(userId);
        verify(noticeFindService).find(noticeId);
        verify(commentSaveService).save(comment);
        verify(fileSaveService).save(List.of());
        verify(commentMapper).fromCommentDto(dto, notice, user, null);

        assertThat(notice.getComments()).contains(comment);

    }

    @Test
    @DisplayName("공지사항 댓글 수정 시 작성자와 수정 요청자가 다르면 예외가 발생한다")
    void updateNoticeComment_throwsException_whenUserIsNotOwner() {
        Long userId = 1L;
        Long different = 2L;
        Long noticeId = 1L;
        // given
        User user  = UserTestFixture.createActiveUser1(1L);
        User user2  = UserTestFixture.createActiveUser1(2L);
        Notice notice = NoticeTestFixture.createNotice(noticeId, "제목1");

        CommentDTO.Update dto = new CommentDTO.Update("수정 완료", List.of());

        Comment comment = CommentTestFixture.createComment(commentId, dto.content(), user, notice);

        given(userGetService.find(user2.getId())).willReturn(user2);
        given(noticeFindService.find(notice.getId())).willReturn(notice);
        given(commentFindService.find(comment.getId())).willReturn(comment);

        // when & then
        assertThrows(UserNotMatchException.class, () ->
                noticeCommentUsecase.updateNoticeComment(dto, noticeId, comment.getId(), different)
        );
    }

    @Test
    void deleteNoticeComment() {
    }
}
