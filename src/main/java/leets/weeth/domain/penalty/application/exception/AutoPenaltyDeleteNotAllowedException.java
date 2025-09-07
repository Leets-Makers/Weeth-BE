package leets.weeth.domain.penalty.application.exception;

import leets.weeth.global.common.exception.BusinessLogicException;

public class AutoPenaltyDeleteNotAllowedException extends BusinessLogicException {
    public AutoPenaltyDeleteNotAllowedException(){
        super(400, "자동 생성된 패널티는 삭제할 수 없습니다");
    }

}
