package leets.weeth.global.sas.config.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import leets.weeth.global.auth.authentication.ErrorMessage;
import leets.weeth.global.common.response.CommonResponse;
import leets.weeth.global.sas.application.property.OauthProviderProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProviderAwareEntryPoint implements AuthenticationEntryPoint {

    private final OauthProviderProperties oauthProps;

    private static final String PROVIDER_PARAMETER = "provider";
    private static final String KAKAO = "kakao";

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException {

        // 1) provider 파라미터 파싱 (없으면 kakao 기본)
        String provider = Optional.ofNullable(req.getParameter(PROVIDER_PARAMETER))
                .orElse(KAKAO);

        OauthProviderProperties.Provider cfg = oauthProps.getProviders().get(provider);

        // 2) 잘못된 provider 처리
        if (cfg == null) {
            setResponse(res);
            return;
        }

        // 3) 리다이렉트 URL 구성 -> 전략 패턴으로 리팩토링
        String redirect = UriComponentsBuilder.fromHttpUrl(cfg.getAuthorizeUri())
                .queryParam("client_id", cfg.getClientId())
                .queryParam("redirect_uri", cfg.getRedirectUri())
                .queryParam("response_type", "code")
                .build()
                .encode()
                .toUriString();

        res.sendRedirect(redirect);
    }

    private void setResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String message = new ObjectMapper().writeValueAsString(CommonResponse.createFailure(ErrorMessage.SC_BAD_REQUEST_PROVIDER.getCode(), ErrorMessage.SC_BAD_REQUEST_PROVIDER.getMessage()));
        response.getWriter().write(message);
    }
}
