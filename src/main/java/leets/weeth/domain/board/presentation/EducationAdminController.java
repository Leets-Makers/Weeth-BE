package leets.weeth.domain.board.presentation;

import static leets.weeth.domain.board.presentation.ResponseMessage.EDUCATION_UPDATED_SUCCESS;
import static leets.weeth.domain.board.presentation.ResponseMessage.POST_CREATED_SUCCESS;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import leets.weeth.domain.board.application.dto.PostDTO;
import leets.weeth.domain.board.application.usecase.PostUsecase;
import leets.weeth.domain.user.application.exception.UserNotMatchException;
import leets.weeth.global.auth.annotation.CurrentUser;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "EDUCATION ADMIN", description = "[ADMIN] 공지사항 교육자료 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/educations")
public class EducationAdminController {
    private final PostUsecase postUsecase;

    @PostMapping("/education")
    @Operation(summary = "교육자료 생성")
    public CommonResponse<String> saveEducation(@RequestBody @Valid PostDTO.SaveEducation dto, @Parameter(hidden = true) @CurrentUser Long userId) {
        postUsecase.saveEducation(dto, userId);

        return CommonResponse.createSuccess(POST_CREATED_SUCCESS.getMessage());
    }

    @PatchMapping(value = "/{boardId}/education")
    @Operation(summary="교육자료 게시글 수정")
    public CommonResponse<String> update(@PathVariable Long boardId,
                                         @RequestBody @Valid PostDTO.UpdateEducation dto,
                                         @Parameter(hidden = true) @CurrentUser Long userId) throws UserNotMatchException {
        postUsecase.updateEducation(boardId, dto, userId);
        return CommonResponse.createSuccess(EDUCATION_UPDATED_SUCCESS.getMessage());
    }
}
