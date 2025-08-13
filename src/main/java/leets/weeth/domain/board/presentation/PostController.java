package leets.weeth.domain.board.presentation;

import static leets.weeth.domain.board.presentation.ResponseMessage.EDUCATION_SEARCH_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.POST_CREATED_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.POST_DELETED_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.POST_EDU_FIND_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.POST_FIND_ALL_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.POST_FIND_BY_ID_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.POST_PART_FIND_ALL_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.POST_SEARCH_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.POST_UPDATED_SUCCESS;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.board.application.dto.PartPostDTO;
import leets.weeth.domain.board.application.dto.PostDTO;
import leets.weeth.domain.board.application.usecase.PostUsecase;
import leets.weeth.domain.board.domain.entity.enums.Part;
import leets.weeth.domain.user.application.exception.UserNotMatchException;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BOARD", description = "게시판 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class PostController {

    private final PostUsecase postUsecase;

    @PostMapping
    @Operation(summary="파트 게시글 생성 (스터디 로그, 아티클)")
    public CommonResponse<String> save(@RequestBody @Valid PostDTO.Save dto, @Parameter(hidden = true) @CurrentUser Long userId) {
        postUsecase.save(dto, userId);

        return CommonResponse.createSuccess(POST_CREATED_SUCCESS.getMessage());
    }

    @GetMapping
    @Operation(summary="게시글 목록 조회 [무한스크롤]")
    public CommonResponse<Slice<PostDTO.ResponseAll>> findPosts(@RequestParam("pageNumber") int pageNumber,
                                                                      @RequestParam("pageSize") int pageSize) {
        return CommonResponse.createSuccess(POST_FIND_ALL_SUCCESS.getMessage(), postUsecase.findPosts(pageNumber, pageSize));
    }

    @GetMapping("/part")
    @Operation(summary="파트별 스터디 게시글 목록 조회 [무한스크롤]")
    public CommonResponse<Slice<PostDTO.ResponseAll>> findPartPosts(@ModelAttribute @Valid PartPostDTO dto, @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        Slice<PostDTO.ResponseAll> response = postUsecase.findPartPosts(dto, pageNumber, pageSize);

        return CommonResponse.createSuccess(POST_PART_FIND_ALL_SUCCESS.getMessage(), response);
    }

    @GetMapping("/education")
    @Operation(summary="교육자료 조회 [무한스크롤]")
    public CommonResponse<Slice<PostDTO.ResponseEducationAll>> findEducationMaterials(@RequestParam Part part, @RequestParam(required = false) Integer cardinalNumber, @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize, @Parameter(hidden = true) @CurrentUser Long userId) {

        return CommonResponse.createSuccess(POST_EDU_FIND_SUCCESS.getMessage(), postUsecase.findEducationPosts(userId, part, cardinalNumber, pageNumber, pageSize));
    }

    @GetMapping("/{boardId}")
    @Operation(summary="특정 게시글 조회")
    public CommonResponse<PostDTO.Response> findPost(@PathVariable Long boardId) {
        return CommonResponse.createSuccess(POST_FIND_BY_ID_SUCCESS.getMessage(),postUsecase.findPost(boardId));
    }

    @GetMapping("/part/studies")
    @Operation(summary="파트별 스터디 이름 목록 조회")
    public CommonResponse<PostDTO.ResponseStudyNames> findStudyNames(@RequestParam Part part) {

        return CommonResponse.createSuccess(ResponseMessage.POST_STUDY_NAMES_FIND_SUCCESS.getMessage(), postUsecase.findStudyNames(part));
    }

    @GetMapping("/search/part")
    @Operation(summary="파트 게시글 검색 [무한스크롤]")
    public CommonResponse<Slice<PostDTO.ResponseAll>> findPost(@RequestParam String keyword, @RequestParam("pageNumber") int pageNumber,
                                                                    @RequestParam("pageSize") int pageSize) {
        return CommonResponse.createSuccess(POST_SEARCH_SUCCESS.getMessage(),postUsecase.searchPost(keyword, pageNumber, pageSize));
    }

    @GetMapping("/search/education")
    @Operation(summary="교육자료 검색 [무한스크롤]")
    public CommonResponse<Slice<PostDTO.ResponseEducationAll>> findEducation(@RequestParam String keyword, @RequestParam("pageNumber") int pageNumber,
                                                               @RequestParam("pageSize") int pageSize) {
        return CommonResponse.createSuccess(EDUCATION_SEARCH_SUCCESS.getMessage(),postUsecase.searchEducation(keyword, pageNumber, pageSize));
    }

    @PatchMapping(value = "/{boardId}/part")
    @Operation(summary="파트 게시글 수정")
    public CommonResponse<String> update(@PathVariable Long boardId,
                                         @RequestBody @Valid PostDTO.Update dto,
                                         @Parameter(hidden = true) @CurrentUser Long userId) throws UserNotMatchException {
        postUsecase.update(boardId, dto, userId);
        return CommonResponse.createSuccess(POST_UPDATED_SUCCESS.getMessage());
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary="특정 게시글 삭제")
    public CommonResponse<String> delete(@PathVariable Long boardId, @Parameter(hidden = true) @CurrentUser Long userId) throws UserNotMatchException {
        postUsecase.delete(boardId, userId);
        return CommonResponse.createSuccess(POST_DELETED_SUCCESS.getMessage());
    }

}
