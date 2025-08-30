package leets.weeth.domain.penalty.application.usecase;

import jakarta.transaction.Transactional;
import leets.weeth.domain.penalty.application.dto.PenaltyDTO;
import leets.weeth.domain.penalty.application.mapper.PenaltyMapper;
import leets.weeth.domain.penalty.domain.entity.Penalty;
import leets.weeth.domain.penalty.domain.entity.enums.PenaltyType;
import leets.weeth.domain.penalty.domain.service.PenaltyDeleteService;
import leets.weeth.domain.penalty.domain.service.PenaltyFindService;
import leets.weeth.domain.penalty.domain.service.PenaltySaveService;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.entity.UserCardinal;
import leets.weeth.domain.user.domain.service.CardinalGetService;
import leets.weeth.domain.user.domain.service.UserCardinalGetService;
import leets.weeth.domain.user.domain.service.UserGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PenaltyUsecaseImpl implements PenaltyUsecase{

    private final PenaltySaveService penaltySaveService;
    private final PenaltyFindService penaltyFindService;
    private final PenaltyDeleteService penaltyDeleteService;

    private final UserGetService userGetService;

    private final UserCardinalGetService userCardinalGetService;
    private final CardinalGetService cardinalGetService;

    private final PenaltyMapper mapper;

    @Override
    @Transactional
    public void save(PenaltyDTO.Save dto) {
        User user = userGetService.find(dto.userId());
        Cardinal cardinal = cardinalGetService.findByUserSide(dto.cardinal());
        userCardinalGetService.validateHasCardinal(user, cardinal);

        Penalty penalty = mapper.fromPenaltyDto(dto, user, cardinal);

        penaltySaveService.save(penalty);

        if(penalty.getPenaltyType().equals(PenaltyType.PENALTY)){
            user.incrementPenaltyCount();
        }
    }

    @Override
    @Transactional
    public void update(PenaltyDTO.Update dto) {
        Penalty penalty = penaltyFindService.find(dto.penaltyId());
        User user = userGetService.find(penalty.getUser().getId());

        if(dto.penaltyType() != null && penalty.getPenaltyType() != dto.penaltyType()){
            penalty.updatePenaltyType(dto.penaltyType());
            if(penalty.getPenaltyType().equals(PenaltyType.PENALTY)){
                user.incrementPenaltyCount();
            }
            else{
                user.decrementPenaltyCount();
            }
        }

        if(dto.penaltyDescription() != null && !dto.penaltyDescription().isBlank()){
            penalty.updatePenaltyDescription(dto.penaltyDescription());
        }

        if(dto.cardinal() != null){
            Cardinal cardinal = cardinalGetService.findByUserSide(dto.cardinal());
            userCardinalGetService.validateHasCardinal(user, cardinal);

            penalty.updateCardinal(cardinal);
        }
    }

    @Override
    public List<PenaltyDTO.Response> find() {
        List<Penalty> penalties = penaltyFindService.findAll();

        Map<Long, List<Penalty>> penaltiesByUser = penalties.stream()
                .collect(Collectors.groupingBy(penalty -> penalty.getUser().getId()));

        return penaltiesByUser.entrySet().stream()
                .map(entry -> toPenaltyDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(PenaltyDTO.Response::userId))
                .toList();
    }

    @Override
    public PenaltyDTO.Response find(Long userId) {
        User user = userGetService.find(userId);
        List<Penalty> penalties = penaltyFindService.findAll(userId);

        return toPenaltyDto(userId, penalties);
    }

    @Override
    @Transactional
    public void delete(Long penaltyId) {
        Penalty penalty = penaltyFindService.find(penaltyId);
        penaltyDeleteService.delete(penaltyId);

        if(penalty.getPenaltyType().equals(PenaltyType.PENALTY)){
            penalty.getUser().decrementPenaltyCount();
        }
    }

    private PenaltyDTO.Response toPenaltyDto(Long userId, List<Penalty> penalties) {
        User user = userGetService.find(userId);
        List<UserCardinal> userCardinals = userCardinalGetService.getUserCardinals(user);

        List<PenaltyDTO.Penalties> penaltyDTOs = penalties.stream()
                .map(mapper::toPenalties)
                .toList();

        Integer penaltyCount = user.getPenaltyCount() + penaltyFindService.countWarningByUserId(userId)/2;

        return mapper.toPenaltyDto(user, penaltyDTOs, userCardinals, penaltyCount);
    }

}
