package leets.weeth.global.sas.config.grant;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

@Getter
public class AppleIdentityTokenAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private final String appleIdentityToken;

    public AppleIdentityTokenAuthenticationToken(
            String appleIdentityToken,
            Authentication clientPrincipal,
            Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType("apple_identity_token"), clientPrincipal, additionalParameters);
        this.appleIdentityToken = appleIdentityToken;
    }
}