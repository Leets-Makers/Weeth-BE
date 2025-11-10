package leets.weeth.global.sas.config.grant;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

public final class AppleGrantType {
    public static final AuthorizationGrantType APPLE_IDENTITY_TOKEN =
            new AuthorizationGrantType("apple_identity_token");

    private AppleGrantType() {}
}