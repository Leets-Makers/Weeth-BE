package leets.weeth.global.sas.application.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class AppleLoginException extends OAuth2AuthenticationException {
    public AppleLoginException(String message) {
        super(new OAuth2Error(ErrorMessage.APPLE_AUTH_ERROR.getCode(), message, null));
    }
}