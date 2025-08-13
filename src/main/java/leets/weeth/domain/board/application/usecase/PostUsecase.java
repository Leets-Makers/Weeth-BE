package leets.weeth.domain.board.application.usecase;

import leets.weeth.domain.board.application.dto.PartPostDTO;
import leets.weeth.domain.board.application.dto.PostDTO;
import leets.weeth.domain.board.domain.entity.enums.Part;
import leets.weeth.domain.user.application.exception.UserNotMatchException;
import org.springframework.data.domain.Slice;


public interface PostUsecase {

    void save(PostDTO.Save request, Long userId);

    void saveEducation(PostDTO.SaveEducation request, Long userId);

    PostDTO.Response findPost(Long postId);

    Slice<PostDTO.ResponseAll> findPosts(int pageNumber, int pageSize);

    Slice<PostDTO.ResponseAll> findPartPosts(PartPostDTO dto, int pageNumber, int pageSize);

    Slice<PostDTO.ResponseEducationAll> findEducationPosts(Long userId, Integer cardinalNumber, int pageNumber, int pageSize);

    PostDTO.ResponseStudyNames findStudyNames(Part part);

    void update(Long postId, PostDTO.Update dto, Long userId) throws UserNotMatchException;

    void updateEducation(Long postId, PostDTO.UpdateEducation dto, Long userId) throws UserNotMatchException;

    void delete(Long postId, Long userId) throws UserNotMatchException;

    Slice<PostDTO.ResponseAll> searchPost(String keyword, int pageNumber, int pageSize);
}
