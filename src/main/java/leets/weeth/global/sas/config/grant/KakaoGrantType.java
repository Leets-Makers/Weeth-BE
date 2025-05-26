package leets.weeth.global.sas.config.grant;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

public final class KakaoGrantType {
    public static final AuthorizationGrantType KAKAO_ACCESS_TOKEN =
            new AuthorizationGrantType("kakao_access_token");

    private KakaoGrantType() {}
}
