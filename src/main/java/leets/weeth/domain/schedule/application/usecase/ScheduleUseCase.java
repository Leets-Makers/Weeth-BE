package leets.weeth.domain.schedule.application.usecase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static leets.weeth.domain.schedule.application.dto.ScheduleDTO.Response;

public interface ScheduleUseCase {

    List<Response> findByMonthly(LocalDateTime start, LocalDateTime end);

    Map<Integer, List<Response>> findByYearly(LocalDateTime start, LocalDateTime end);

}
