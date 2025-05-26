package leets.weeth.global.sas.application.mapper;

import leets.weeth.global.sas.domain.entity.*;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.util.Map;

public class Oauth2AuthorizationTokenExtractor {
    static AuthorizationCode extractAuthorizationCode(OAuth2Authorization authorization) {
        AuthorizationCode authorizationCode = null;

        if (authorization.getToken(OAuth2AuthorizationCode.class) != null) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> oauth2AuthorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);

            authorizationCode = AuthorizationCode.builder()
                    .tokenValue(oauth2AuthorizationCode.getToken().getTokenValue())
                    .issuedAt(oauth2AuthorizationCode.getToken().getIssuedAt())
                    .expiresAt(oauth2AuthorizationCode.getToken().getExpiresAt())
                    .invalidated(oauth2AuthorizationCode.isInvalidated())
                    .build();
        }

        return authorizationCode;
    }

    static AccessToken extractAccessToken(OAuth2Authorization authorization) {
        AccessToken accessToken = null;

        if (authorization.getAccessToken() != null) {
            OAuth2Authorization.Token<OAuth2AccessToken> oauth2AccessToken = authorization.getAccessToken();
            OAuth2TokenFormat tokenFormat = null;

            if (OAuth2TokenFormat.SELF_CONTAINED.getValue().equals(oauth2AccessToken.getMetadata(OAuth2TokenFormat.class.getName()))) {
                tokenFormat = OAuth2TokenFormat.SELF_CONTAINED;
            } else if (OAuth2TokenFormat.REFERENCE.getValue().equals(oauth2AccessToken.getMetadata(OAuth2TokenFormat.class.getName()))) {
                tokenFormat = OAuth2TokenFormat.REFERENCE;
            }
            accessToken = AccessToken.builder()
                    .tokenValue(oauth2AccessToken.getToken().getTokenValue())
                    .issuedAt(oauth2AccessToken.getToken().getIssuedAt())
                    .expiresAt(oauth2AccessToken.getToken().getExpiresAt())
                    .invalidated(oauth2AccessToken.isInvalidated())
                    .tokenType(oauth2AccessToken.getToken().getTokenType())
                    .scopes(oauth2AccessToken.getToken().getScopes())
                    .tokenFormat(tokenFormat)
                    .claims(ClaimsHolder.builder()
                            .claims(oauth2AccessToken.getClaims() != null ? oauth2AccessToken.getClaims() : Map.of())
                            .build())
                    .build();
        }
        return accessToken;
    }

    static RefreshToken extractRefreshToken(OAuth2Authorization authorization) {
        RefreshToken refreshToken = null;
        if (authorization.getRefreshToken() != null) {
            OAuth2Authorization.Token<OAuth2RefreshToken> oauth2RefreshToken = authorization.getRefreshToken();

            refreshToken = RefreshToken.builder()
                    .tokenValue(oauth2RefreshToken.getToken().getTokenValue())
                    .issuedAt(oauth2RefreshToken.getToken().getIssuedAt())
                    .expiresAt(oauth2RefreshToken.getToken().getExpiresAt())
                    .invalidated(oauth2RefreshToken.isInvalidated())
                    .build();
        }

        return refreshToken;
    }

    static IdToken extractIdToken(OAuth2Authorization authorization) {
        IdToken idToken = null;
        if (authorization.getToken(OidcIdToken.class) != null) {
            OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
            idToken = IdToken.builder()
                    .tokenValue(oidcIdToken.getToken().getTokenValue())
                    .issuedAt(oidcIdToken.getToken().getIssuedAt())
                    .expiresAt(oidcIdToken.getToken().getExpiresAt())
                    .invalidated(oidcIdToken.isInvalidated())
                    .claims(ClaimsHolder.builder()
                            .claims(oidcIdToken.getClaims() != null ? oidcIdToken.getClaims() : Map.of())
                            .build())
                    .build();
        }
        return idToken;
    }
}
