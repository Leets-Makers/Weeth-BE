package leets.weeth.domain.schedule.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import leets.weeth.domain.schedule.application.usecase.ScheduleUseCase;
import leets.weeth.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static leets.weeth.domain.schedule.application.dto.ScheduleDTO.Response;
import static leets.weeth.domain.schedule.presentation.ResponseMessage.SCHEDULE_MONTHLY_FIND_SUCCESS;
import static leets.weeth.domain.schedule.presentation.ResponseMessage.SCHEDULE_YEARLY_FIND_SUCCESS;

@Tag(name = "SCHEDULE", description = "캘린더 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleUseCase scheduleUseCase;

    @GetMapping("/monthly")
    @Operation(summary="월별 일정 조회")
    public CommonResponse<List<Response>> findByMonthly(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return CommonResponse.createSuccess(SCHEDULE_MONTHLY_FIND_SUCCESS.getMessage(),scheduleUseCase.findByMonthly(start, end));
    }

    @GetMapping("/yearly")
    @Operation(summary="연도별 일정 조회")
    public CommonResponse<Map<Integer, List<Response>>> findByYearly(@RequestParam Integer year,
                                                                     @RequestParam Integer semester) {
        return CommonResponse.createSuccess(SCHEDULE_YEARLY_FIND_SUCCESS.getMessage(),scheduleUseCase.findByYearly(year, semester));
    }
}
