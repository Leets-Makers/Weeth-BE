package leets.weeth.domain.penalty.domain.repository;

import leets.weeth.domain.penalty.domain.entity.Penalty;
import leets.weeth.domain.penalty.domain.entity.enums.PenaltyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    Integer countByUserIdAndCardinalIdAndPenaltyType(Long userId, Long cardinalId, PenaltyType penaltyType);

    List<Penalty> findByUserIdAndCardinalId(Long userId, Long cardinalId);
}
