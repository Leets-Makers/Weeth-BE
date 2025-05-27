package leets.weeth.global.sas.application.exception;

import leets.weeth.global.common.exception.BusinessLogicException;

public class Oauth2JwtTokenException extends BusinessLogicException {
    public Oauth2JwtTokenException() {
        super(500, "Jwt 토큰 생성 중 문제가 발생했습니다.");
    }
}
