package leets.weeth.domain.user.domain.repository;

import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.enums.CardinalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardinalRepository extends JpaRepository<Cardinal, Long> {

    Optional<Cardinal> findByCardinalNumber(Integer cardinal);

    Optional<Cardinal> findByYearAndSemester(Integer year, Integer semester);

    List<Cardinal> findAllByStatus(CardinalStatus cardinalStatus);

    List<Cardinal> findAllByOrderByCardinalNumberAsc();

    Cardinal findFirstByStatusOrderByCardinalNumberDesc(CardinalStatus status);
}
