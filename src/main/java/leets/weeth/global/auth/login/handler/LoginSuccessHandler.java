package leets.weeth.global.auth.login.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import leets.weeth.domain.user.domain.entity.User;
import leets.weeth.domain.user.domain.service.UserGetService;
import leets.weeth.global.auth.jwt.application.dto.JwtDto;
import leets.weeth.global.auth.jwt.application.usecase.JwtManageUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtManageUseCase jwtManageUseCase;
    private final UserGetService userGetService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = extractEmail(authentication); // 인증 정보에서 email 추출
        // 유저 캐싱 도입
        User user = userGetService.find(email);
        Long userId = user.getId();

        // 토큰 발급 및 레디스에 저장
        JwtDto token = jwtManageUseCase.create(userId, email);

        // 바디에 담아서 보내기
        jwtManageUseCase.sendToken(token, response); // 응답 헤더에 AccessToken, RefreshToken 실어서 응답
    }

    private String extractEmail(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

}

