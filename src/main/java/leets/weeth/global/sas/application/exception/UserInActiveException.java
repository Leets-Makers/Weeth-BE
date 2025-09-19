package leets.weeth.global.sas.application.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

public class UserInActiveException extends OAuth2AuthenticationException {
    public UserInActiveException() {
        super(new OAuth2Error(ErrorMessage.USER_INACTIVE.getCode(), ErrorMessage.USER_INACTIVE.getDescription(), null));
    }
}
