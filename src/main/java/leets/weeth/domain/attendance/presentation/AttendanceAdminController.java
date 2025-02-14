package leets.weeth.domain.attendance.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import leets.weeth.domain.attendance.application.dto.AttendanceDTO;
import leets.weeth.domain.attendance.application.usecase.AttendanceUseCase;
import leets.weeth.domain.schedule.application.dto.MeetingDTO;
import leets.weeth.domain.schedule.application.usecase.MeetingUseCase;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static leets.weeth.domain.attendance.presentation.ResponseMessage.*;

@Tag(name = "ATTENDANCE ADMIN", description = "[ADMIN] 출석 어드민 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/attendances")
public class AttendanceAdminController {

    private final AttendanceUseCase attendanceUseCase;
    private final MeetingUseCase meetingUseCase;

    @PatchMapping
    @Operation(summary="출석 마감")
    public CommonResponse<Void> close(@RequestParam LocalDate now, @RequestParam Integer cardinal) {
        attendanceUseCase.close(now, cardinal);
        return CommonResponse.createSuccess(ATTENDANCE_CLOSE_SUCCESS.getMessage());
    }

    @GetMapping("/meetings/{cardinal}")
    @Operation(summary = "정기모임 조회")
    public CommonResponse<List<MeetingDTO.Info>> getMeetings(@PathVariable Integer cardinal) {
        List<MeetingDTO.Info> response = meetingUseCase.find(cardinal);

        return CommonResponse.createSuccess(MEETING_FIND_SUCCESS.getMessage(), response);
    }

    @GetMapping("/{meetingId}")
    @Operation(summary = "모든 인원 정기모임 출석 정보 조회")
    public CommonResponse<List<AttendanceDTO.AttendanceInfo>> getAllAttendance(@PathVariable Long meetingId) {
        return CommonResponse.createSuccess(ATTENDANCE_FIND_DETAIL_SUCCESS.getMessage(), attendanceUseCase.findAllAttendanceByMeeting(meetingId));
    }

    @PatchMapping("/status")
    @Operation(summary = "모든 인원 정기모임 개별 출석 상태 수정")
    public CommonResponse<Void> updateAttendanceStatus(@RequestBody List<AttendanceDTO.UpdateStatus> attendanceUpdates) {
        attendanceUseCase.updateAttendanceStatus(attendanceUpdates);
        return CommonResponse.createSuccess(ATTENDANCE_UPDATED_SUCCESS.getMessage());
    }
}
