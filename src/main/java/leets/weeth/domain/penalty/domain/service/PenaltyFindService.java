package leets.weeth.domain.penalty.domain.service;

import leets.weeth.domain.penalty.domain.entity.Penalty;
import leets.weeth.domain.penalty.domain.entity.enums.PenaltyType;
import leets.weeth.domain.penalty.domain.repository.PenaltyRepository;
import leets.weeth.domain.penalty.application.exception.PenaltyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PenaltyFindService {

    private final PenaltyRepository penaltyRepository;

    public Penalty find(Long penaltyId){
        return penaltyRepository.findById(penaltyId)
                .orElseThrow(PenaltyNotFoundException::new);
    }

    public Integer countWarningByUserId(Long userId) {
        return penaltyRepository.countByUserIdAndPenaltyType(userId, PenaltyType.WARNING);
    }

    public List<Penalty> findAll(Long userId){
        return penaltyRepository.findByUserId(userId);
    }

    public List<Penalty> findAll(){
        return penaltyRepository.findAll();
    }

}
