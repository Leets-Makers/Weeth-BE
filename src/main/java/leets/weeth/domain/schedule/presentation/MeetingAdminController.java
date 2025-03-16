package leets.weeth.domain.schedule.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import leets.weeth.domain.schedule.application.usecase.MeetingUseCase;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static leets.weeth.domain.schedule.presentation.ResponseMessage.MEETING_DELETE_SUCCESS;

@Tag(name = "MEETING ADMIN", description = "[ADMIN] 정기모임 어드민 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/meetings")
public class MeetingAdminController {

    private final MeetingUseCase meetingUseCase;

    @DeleteMapping("/{meetingId}")
    @Operation(summary = "정기모임 삭제")
    public CommonResponse<Void> delete(@PathVariable Long meetingId) {
        meetingUseCase.delete(meetingId);
        return CommonResponse.createSuccess(MEETING_DELETE_SUCCESS.getMessage());
    }
}
