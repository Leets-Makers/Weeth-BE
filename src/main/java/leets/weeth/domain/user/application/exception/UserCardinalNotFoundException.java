package leets.weeth.domain.user.application.exception;

import leets.weeth.global.common.exception.BusinessLogicException;

public class UserCardinalNotFoundException extends BusinessLogicException {
    public UserCardinalNotFoundException() {
        super(404, "사용자가 해당 기수에 속해있지 않습니다.");
    }
}
