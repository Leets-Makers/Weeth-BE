package leets.weeth.global.sas.application.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class UserNotFoundException extends OAuth2AuthenticationException {
    public UserNotFoundException() {
        super(new OAuth2Error(ErrorMessage.USER_NOT_FOUND.getCode(), ErrorMessage.USER_NOT_FOUND.getDescription(), null));
    }
}
