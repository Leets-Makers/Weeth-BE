package leets.weeth.domain.board.application.usecase;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import leets.weeth.domain.board.application.dto.PostDTO;
import leets.weeth.domain.board.application.exception.CategoryAccessDeniedException;
import leets.weeth.domain.board.application.exception.NoSearchResultException;
import leets.weeth.domain.board.application.exception.PageNotFoundException;
import leets.weeth.domain.board.application.mapper.PostMapper;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.board.domain.entity.enums.Category;
import leets.weeth.domain.board.domain.entity.enums.Part;
import leets.weeth.domain.board.domain.service.PostDeleteService;
import leets.weeth.domain.board.domain.service.PostFindService;
import leets.weeth.domain.board.domain.service.PostSaveService;
import leets.weeth.domain.board.domain.service.PostUpdateService;
import leets.weeth.domain.comment.application.dto.CommentDTO;
import leets.weeth.domain.comment.application.mapper.CommentMapper;
import leets.weeth.domain.comment.domain.entity.Comment;
import leets.weeth.domain.file.application.dto.response.FileResponse;
import leets.weeth.domain.file.application.mapper.FileMapper;
import leets.weeth.domain.file.domain.entity.File;
import leets.weeth.domain.file.domain.service.FileDeleteService;
import leets.weeth.domain.file.domain.service.FileGetService;
import leets.weeth.domain.file.domain.service.FileSaveService;
import leets.weeth.domain.user.application.exception.UserNotMatchException;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.entity.enums.Role;
import leets.weeth.domain.user.domain.service.CardinalGetService;
import leets.weeth.domain.user.domain.service.UserCardinalGetService;
import leets.weeth.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostUseCaseImpl implements PostUsecase {

    private final PostSaveService postSaveService;
    private final PostFindService postFindService;
    private final PostUpdateService postUpdateService;
    private final PostDeleteService postDeleteService;

    private final UserGetService userGetService;
    private final UserCardinalGetService userCardinalGetService;
    private final CardinalGetService cardinalGetService;

    private final FileSaveService fileSaveService;
    private final FileGetService fileGetService;
    private final FileDeleteService fileDeleteService;

    private final PostMapper mapper;
    private final FileMapper fileMapper;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public void save(PostDTO.Save request, Long userId) {
        User user = userGetService.find(userId);

        if (request.category() == Category.Education
                && !user.hasRole(Role.ADMIN)) {
            throw new CategoryAccessDeniedException();
        }

        Cardinal latest = cardinalGetService.getLatestInProgress();
        Part userPart = user.getUserPart();

        Post post = mapper.fromPostDto(request, user);
        post.updatePart(userPart);
        post.updateCardinalNumber(latest);
        postSaveService.save(post);

        List<File> files = fileMapper.toFileList(request.files(), post);
        fileSaveService.save(files);
    }

    @Override
    public PostDTO.Response findPost(Long postId) {
        Post post = postFindService.find(postId);

        List<FileResponse> response = getFiles(postId).stream()
                .map(fileMapper::toFileResponse)
                .toList();


        return mapper.toPostDto(post, response, filterParentComments(post.getComments()));
    }

    @Override
    public Slice<PostDTO.ResponseAll> findPosts(int pageNumber, int pageSize) {
        validatePageNumber(pageNumber);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Slice<Post> posts = postFindService.findRecentPosts(pageable);

        return posts.map(post->mapper.toAll(post, checkFileExistsByPost(post.id)));
    }

    @Override
    public Slice<PostDTO.ResponseAll> findPartPosts(Part part, Category category, Integer cardinalNumber, String studyName, Integer week, int pageNumber, int pageSize) {
        validatePageNumber(pageNumber);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Slice<Post> posts = postFindService.findByPartAndOptionalFilters(part, category, cardinalNumber, studyName, week, pageable);

        return posts.map(post->mapper.toAll(post, checkFileExistsByPost(post.id)));
    }

    @Override
    public Slice<PostDTO.ResponseAll> findEducationByUser(Long userId, Integer cardinalNumber, int pageNumber, int pageSize) {
        User user = userGetService.find(userId);
        Part userPart = user.getUserPart();

        Integer targetCardinal = Optional.ofNullable(cardinalNumber)
                .orElseGet(() -> userCardinalGetService.getCurrentCardinal(user).getCardinalNumber());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Slice<Post> posts = postFindService.findByPartAndOptionalFilters(
                userPart, Category.Education, targetCardinal, null, null, pageable
        );

        return posts.map(post -> mapper.toAll(post, checkFileExistsByPost(post.getId())));
    }

    @Override
    public Slice<PostDTO.ResponseAll> searchPost(String keyword, int pageNumber, int pageSize){
        validatePageNumber(pageNumber);

        keyword = keyword.strip();  // 문자열 앞뒤 공백 제거

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Slice<Post> posts = postFindService.search(keyword, pageable);

        if(posts.isEmpty()){
            throw new NoSearchResultException();
        }

        return posts.map(post->mapper.toAll(post, checkFileExistsByPost(post.id)));
    }

    @Override
    @Transactional
    public void update(Long postId, PostDTO.Update dto, Long userId) {
        Post post = validateOwner(postId, userId);

        List<File> fileList = getFiles(postId);
        fileDeleteService.delete(fileList);

        List<File> files = fileMapper.toFileList(dto.files(), post);
        fileSaveService.save(files);

        postUpdateService.update(post, dto);
    }

    @Override
    @Transactional
    public void delete(Long postId, Long userId) {
        validateOwner(postId, userId);

        List<File> fileList = getFiles(postId);
        fileDeleteService.delete(fileList);

        postDeleteService.delete(postId);
    }

    private List<File> getFiles(Long postId) {
        return fileGetService.findAllByPost(postId);
    }

    private Post validateOwner(Long postId, Long userId) {
        Post post = postFindService.find(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new UserNotMatchException();
        }
        return post;
    }

    public boolean checkFileExistsByPost(Long postId){
        return !fileGetService.findAllByPost(postId).isEmpty();
    }

    private List<CommentDTO.Response> filterParentComments(List<Comment> comments) {
        Map<Long, List<Comment>> commentMap = comments.stream()
                .filter(comment -> comment.getParent() != null)
                .collect(Collectors.groupingBy(comment -> comment.getParent().getId()));

        return comments.stream()
                .filter(comment -> comment.getParent() == null) // 부모 댓글만 가져오기
                .map(parent -> mapToDtoWithChildren(parent, commentMap))
                .toList();
    }

    private CommentDTO.Response mapToDtoWithChildren(Comment comment, Map<Long, List<Comment>> commentMap) {
        List<CommentDTO.Response> children = commentMap.getOrDefault(comment.getId(), Collections.emptyList())
                .stream()
                .map(child -> mapToDtoWithChildren(child, commentMap))
                .collect(Collectors.toList());

        return commentMapper.toCommentDto(comment, children);
    }

    private void validatePageNumber(int pageNumber){
        if (pageNumber < 0) {
            throw new PageNotFoundException();
        }
    }

}
