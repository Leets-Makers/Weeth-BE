package leets.weeth.domain.board.application.usecase;

import leets.weeth.domain.board.application.dto.PostDTO;
import leets.weeth.domain.board.application.exception.CategoryAccessDeniedException;
import leets.weeth.domain.board.application.mapper.PostMapper;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
import leets.weeth.domain.board.domain.service.PostDeleteService;
import leets.weeth.domain.board.domain.service.PostFindService;
import leets.weeth.domain.board.domain.service.PostSaveService;
import leets.weeth.domain.board.domain.service.PostUpdateService;
import leets.weeth.domain.board.test.fixture.PostTestFixture;
import leets.weeth.domain.comment.application.mapper.CommentMapper;
import leets.weeth.domain.file.application.mapper.FileMapper;
import leets.weeth.domain.file.domain.service.FileDeleteService;
import leets.weeth.domain.file.domain.service.FileGetService;
import leets.weeth.domain.file.domain.service.FileSaveService;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.service.CardinalGetService;
import leets.weeth.domain.user.domain.service.UserCardinalGetService;
import leets.weeth.domain.user.domain.service.UserGetService;
import leets.weeth.domain.user.test.fixture.CardinalTestFixture;
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
class PostUseCaseImplTest {

    @InjectMocks private PostUseCaseImpl postUseCase;

    @Mock private PostSaveService postSaveService;
    @Mock private PostFindService postFindService;
    @Mock private PostUpdateService postUpdateService;
    @Mock private PostDeleteService postDeleteService;

    @Mock private UserGetService userGetService;
    @Mock private UserCardinalGetService userCardinalGetService;
    @Mock private CardinalGetService cardinalGetService;

    @Mock private FileSaveService fileSaveService;
    @Mock private FileGetService fileGetService;
    @Mock private FileDeleteService fileDeleteService;

    @Mock private PostMapper mapper;
    @Mock private FileMapper fileMapper;
    @Mock private CommentMapper commentMapper;


    @Test
    @DisplayName("교육 게시글 저장 성공")
    void saveEducation() {
        Long userId = 1L;
        long postId = 1L;
        // given
        PostDTO.SaveEducation request = new PostDTO.SaveEducation("제목1", "내용",
                List.of(Part.BE), 1, List.of());

        User user  = UserTestFixture.createActiveUser1(1L);
        Post post = PostTestFixture.createPost(postId, "제목1", Category.Education);
        
        given(userGetService.find(userId)).willReturn(user);
        given(mapper.fromEducationDto(request, user)).willReturn(post);
        given(postSaveService.save(post)).willReturn(post);
        given(fileMapper.toFileList(request.files(), post)).willReturn(List.of());
        given(mapper.toSaveResponse(post)).willReturn(new PostDTO.SaveResponse(postId));

        // when
        PostDTO.SaveResponse response = postUseCase.saveEducation(request, userId);

        // then
        assertThat(response.id()).isEqualTo(postId);
        verify(userGetService).find(userId);
        verify(postSaveService).save(post);
        verify(mapper).toSaveResponse(post);

    }

    @Test
    @DisplayName("관리자 권한이 없는 사용자가 교육 게시글 생성 시 예외를 던진다")
    void saveEducation_unauthorizedUser_throwsException(){
        Long userId = 1L;
        PostDTO.Save request = new PostDTO.Save("제목", "내용", Category.Education,
                null, 1, Part.BE, 1, List.of());
        User user  = UserTestFixture.createActiveUser1(1L);

        given(userGetService.find(userId)).willReturn(user);

        // when & then
        assertThrows(CategoryAccessDeniedException.class, () -> postUseCase.save(request, userId));

    }

    @Test
    void findPartPosts() {

    }

    @Test
    void findEducationPosts() {
    }

    @Test
    void findStudyNames() {
    }

    @Test
    void searchEducation() {
    }

    @Test
    void updateEducation() {
    }

    @Test
    void checkFileExistsByPost() {
    }
}