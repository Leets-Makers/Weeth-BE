package leets.weeth.global.sas.application.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class KakaoLoginException extends OAuth2AuthenticationException {
    public KakaoLoginException(String message) {
        super(new OAuth2Error(ErrorMessage.KAKAO_AUTH_ERROR.getCode(), message, null));

    }
}
