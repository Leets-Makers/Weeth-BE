package leets.weeth.global.sas.config.grant;

import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.service.UserGetService;
import leets.weeth.global.auth.kakao.KakaoAuthService;
import leets.weeth.global.auth.kakao.dto.KakaoUserInfoResponse;
import leets.weeth.global.sas.application.exception.UserInActiveException;
import leets.weeth.global.sas.application.exception.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

@Component
public class KakaoAuthenticationProvider extends CustomAuthenticationProvider<KakaoUserInfoResponse> {

    private final KakaoAuthService kakaoAuthService;
    private final UserGetService userGetService;

    public KakaoAuthenticationProvider(
            KakaoAuthService kakaoAuthService,
            UserGetService userGetService,
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator
    ) {
        super(authorizationService, tokenGenerator);
        this.kakaoAuthService = kakaoAuthService;
        this.userGetService = userGetService;
    }

    @Override
    protected AuthorizationGrantType getGrantTokenType() {
        return KakaoGrantType.KAKAO_ACCESS_TOKEN;
    }

    @Override
    protected Class<? extends Authentication> getAuthenticationClass() {
        return KakaoAccessTokenAuthenticationToken.class;
    }

    @Override
    protected String extractAccessToken(Authentication authentication) {
        KakaoAccessTokenAuthenticationToken grantAuth = (KakaoAccessTokenAuthenticationToken) authentication;

        return grantAuth.getKakaoAccessToken();
    }

    @Override
    protected KakaoUserInfoResponse getUserInfo(String accessToken) {
        return kakaoAuthService.getUserInfo(accessToken);
    }

    @Override
    protected User getOrLoadUser(KakaoUserInfoResponse userInfo) {
        long kakaoId = userInfo.id();
        User user = userGetService.find(kakaoId)
                .orElseThrow(UserNotFoundException::new);

        if (user.isInactive()) {
            throw new UserInActiveException();
        }

        return user;
    }
}
