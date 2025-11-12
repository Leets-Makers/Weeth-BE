package leets.weeth.global.sas.application.usecase;

import leets.weeth.domain.user.application.exception.UserInActiveException;
import leets.weeth.domain.user.application.exception.UserNotFoundException;
import leets.weeth.domain.user.domain.entity.Cardinal;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.service.UserCardinalGetService;
import leets.weeth.domain.user.domain.service.UserGetService;
import leets.weeth.global.auth.apple.AppleAuthService;
import leets.weeth.global.auth.apple.dto.AppleTokenResponse;
import leets.weeth.global.auth.apple.dto.AppleUserInfo;
import leets.weeth.global.auth.jwt.service.JwtService;
import leets.weeth.global.auth.kakao.KakaoAuthService;
import leets.weeth.global.auth.kakao.dto.KakaoTokenResponse;
import leets.weeth.global.auth.kakao.dto.KakaoUserInfoResponse;
import leets.weeth.global.sas.application.dto.OauthUserInfoResponse;
import leets.weeth.global.sas.application.mapper.OauthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUsecase {

    private final KakaoAuthService kakaoAuthService;
    private final AppleAuthService appleAuthService;
    private final UserGetService userGetService;
    private final JwtService jwtService;
    private final UserCardinalGetService userCardinalGetService;

    private final OauthMapper oauthMapper;

    /*
        추후에 id_token 검증으로 수정하기
     */
    public User login(String authCode) {
        KakaoTokenResponse tokenResponse = kakaoAuthService.getKakaoToken(authCode);
        KakaoUserInfoResponse userInfo = kakaoAuthService.getUserInfo(tokenResponse.access_token());

        long kakaoId = userInfo.id();
        Optional<User> optionalUser = userGetService.find(kakaoId);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(); // -> Weeth 회원가입 페이지로 리다이렉트 (웹뷰, 기기 브라우저)
        }

        User user = optionalUser.get();

        if (user.isInactive()) {
            throw new UserInActiveException(); // -> weeth 가입 승인 페이지로 리 다이렉트 or LEENK 내부 모달이나 토스트 메시지로 가입 승인 중이라고 표시
        }

        return user;
    }

    /*
    필요 없음
     */
    public User appleLogin(String authCode, String idToken) {
        AppleTokenResponse tokenResponse = appleAuthService.getAppleToken(authCode);

        // ID Token 사용
        String token = idToken != null ? idToken : tokenResponse.id_token();
        AppleUserInfo userInfo = appleAuthService.verifyAndDecodeIdToken(token);

        String appleId = userInfo.appleId();
        Optional<User> optionalUser = userGetService.findByAppleId(appleId);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(); // -> Weeth 회원가입 페이지로 리다이렉트
        }

        User user = optionalUser.get();

        if (user.isInactive()) {
            throw new UserInActiveException(); // -> 가입 승인 대기
        }

        return user;
    }

    public OauthUserInfoResponse userInfo(String accessToken) {
        String token = accessToken.substring(7);

        Optional<Long> optionalUserId = jwtService.extractId(token);

        if (optionalUserId.isEmpty()) {
            throw new UserNotFoundException();
        }

        Long userId = optionalUserId.get();
        User user = userGetService.find(userId);

        Cardinal currentCardinal = userCardinalGetService.getCurrentCardinal(user);

        return oauthMapper.toResponse(user, currentCardinal.getCardinalNumber());
    }
}
