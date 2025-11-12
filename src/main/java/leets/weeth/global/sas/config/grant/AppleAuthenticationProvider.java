package leets.weeth.global.sas.config.grant;

import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.service.UserGetService;
import leets.weeth.global.auth.apple.AppleAuthService;
import leets.weeth.global.auth.apple.dto.AppleUserInfo;
import leets.weeth.global.sas.application.exception.AppleLoginException;
import leets.weeth.global.sas.application.exception.UserInActiveException;
import leets.weeth.global.sas.application.exception.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

@Component
public class AppleAuthenticationProvider extends CustomAuthenticationProvider<AppleUserInfo> {

    private final AppleAuthService appleAuthService;
    private final UserGetService userGetService;

    public AppleAuthenticationProvider(
            AppleAuthService appleAuthService,
            UserGetService userGetService,
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator
    ) {
        super(authorizationService, tokenGenerator);
        this.appleAuthService = appleAuthService;
        this.userGetService = userGetService;
    }

    @Override
    protected AuthorizationGrantType getGrantTokenType() {
        return AppleGrantType.APPLE_IDENTITY_TOKEN;
    }

    @Override
    protected Class<? extends Authentication> getAuthenticationClass() {
        return AppleIdentityTokenAuthenticationToken.class;
    }

    @Override
    protected String extractAccessToken(Authentication authentication) {
        AppleIdentityTokenAuthenticationToken grantAuth =
                (AppleIdentityTokenAuthenticationToken) authentication;
        return grantAuth.getAppleIdentityToken();
    }

    @Override
    protected AppleUserInfo getUserInfo(String identityToken) {
        try {
            // Identity Token 검증 및 사용자 정보 추출
            return appleAuthService.verifyAndDecodeIdToken(identityToken);
        } catch (Exception e) {
            throw new AppleLoginException(e.getMessage());
        }
    }

    @Override
    protected User getOrLoadUser(AppleUserInfo userInfo) {
        String appleId = userInfo.appleId();
        User user = userGetService.findByAppleId(appleId)
                .orElseThrow(UserNotFoundException::new);

        if (user.isInactive()) {
            throw new UserInActiveException();
        }

        return user;
    }
}
