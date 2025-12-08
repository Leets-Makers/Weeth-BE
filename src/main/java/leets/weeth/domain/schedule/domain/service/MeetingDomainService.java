package leets.weeth.domain.schedule.domain.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import leets.weeth.domain.schedule.domain.entity.Meeting;

@Service
public class MeetingDomainService {

	public List<Meeting> reorderMeetingsWithThisWeek(List<Meeting> meetings) {

		LocalDate today = LocalDate.now();
		LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate endOfWeek   = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		Meeting thisWeek = meetings.stream()
			.filter(m -> {
				LocalDate date = m.getStart().toLocalDate();
				return !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek);
			})
			.findFirst()
			.orElse(null);

		List<Meeting> result = new ArrayList<>();

		if (thisWeek != null) {
			result.add(thisWeek);
		}

		result.addAll(
			meetings.stream()
				.sorted(Comparator.comparing(Meeting::getStart).reversed())
				.toList()
		);

		return result;
	}

}
