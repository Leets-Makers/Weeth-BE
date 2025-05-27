package leets.weeth.global.sas.application.mapper;

import leets.weeth.global.sas.domain.entity.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.security.Principal;

public class OAuth2AuthorizationMapper {

    public static void mapOAuth2AuthorizationGrantAuthorization(
            OAuth2AuthorizationGrantAuthorization authorizationGrantAuthorization,
            OAuth2Authorization.Builder builder) {

        if (authorizationGrantAuthorization instanceof OidcAuthorizationCodeGrantAuthorization authorizationGrant) {
            mapOidcAuthorizationCodeGrantAuthorization(authorizationGrant, builder);
        } else if (authorizationGrantAuthorization instanceof OAuth2AuthorizationCodeGrantAuthorization authorizationGrant) {
            mapOAuth2AuthorizationCodeGrantAuthorization(authorizationGrant, builder);
        }
    }

    static void mapOidcAuthorizationCodeGrantAuthorization(
            OidcAuthorizationCodeGrantAuthorization authorizationCodeGrantAuthorization,
            OAuth2Authorization.Builder builder) {

        mapOAuth2AuthorizationCodeGrantAuthorization(authorizationCodeGrantAuthorization, builder);
        mapIdToken(authorizationCodeGrantAuthorization.getIdToken(), builder);
    }

    static void mapOAuth2AuthorizationCodeGrantAuthorization(
            OAuth2AuthorizationCodeGrantAuthorization authorizationCodeGrantAuthorization,
            OAuth2Authorization.Builder builder) {

        builder.id(authorizationCodeGrantAuthorization.getId())
                .principalName(authorizationCodeGrantAuthorization.getPrincipalName())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizedScopes(authorizationCodeGrantAuthorization.getAuthorizedScopes());

        if (authorizationCodeGrantAuthorization.getPrincipal() != null) {
            builder.attribute(Principal.class.getName(), authorizationCodeGrantAuthorization.getPrincipal());
        }

        if (authorizationCodeGrantAuthorization.getAuthorizationRequest() != null) {
            builder.attribute(OAuth2AuthorizationRequest.class.getName(),
                    authorizationCodeGrantAuthorization.getAuthorizationRequest());
        }

        mapAuthorizationCode(authorizationCodeGrantAuthorization.getAuthorizationCode(), builder);
        mapAccessToken(authorizationCodeGrantAuthorization.getAccessToken(), builder);
        mapRefreshToken(authorizationCodeGrantAuthorization.getRefreshToken(), builder);
    }

    static void mapAuthorizationCode(AuthorizationCode authorizationCode,
                                     OAuth2Authorization.Builder builder) {
        if (authorizationCode == null) {
            return;
        }
        OAuth2AuthorizationCode oauth2AuthorizationCode = new OAuth2AuthorizationCode(authorizationCode.getTokenValue(),
                authorizationCode.getIssuedAt(), authorizationCode.getExpiresAt());
        builder.token(oauth2AuthorizationCode, (metadata) -> metadata
                .put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, authorizationCode.isInvalidated()));
    }

    static void mapAccessToken(AccessToken accessToken,
                               OAuth2Authorization.Builder builder) {
        if (accessToken == null) {
            return;
        }
        OAuth2AccessToken oauth2AccessToken = new OAuth2AccessToken(accessToken.getTokenType(),
                accessToken.getTokenValue(), accessToken.getIssuedAt(), accessToken.getExpiresAt(),
                accessToken.getScopes());
        builder.token(oauth2AccessToken, (metadata) -> {
            metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, accessToken.isInvalidated());
            metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, accessToken.getClaims().getClaims());
            metadata.put(OAuth2TokenFormat.class.getName(), accessToken.getTokenFormat().getValue());
        });
    }

    static void mapRefreshToken(RefreshToken refreshToken,
                                OAuth2Authorization.Builder builder) {
        if (refreshToken == null) {
            return;
        }
        OAuth2RefreshToken oauth2RefreshToken = new OAuth2RefreshToken(refreshToken.getTokenValue(),
                refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
        builder.token(oauth2RefreshToken, (metadata) -> metadata
                .put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, refreshToken.isInvalidated()));
    }

    static void mapIdToken(IdToken idToken, OAuth2Authorization.Builder builder) {
        if (idToken == null) {
            return;
        }
        OidcIdToken oidcIdToken = new OidcIdToken(idToken.getTokenValue(), idToken.getIssuedAt(),
                idToken.getExpiresAt(), idToken.getClaims().getClaims());
        builder.token(oidcIdToken, (metadata) -> {
            metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, idToken.isInvalidated());
            metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims().getClaims());
        });
    }


}
