package leets.weeth.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderAwareEntryPoint implements AuthenticationEntryPoint {

    private final OauthProviderProperties oauthProps;

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException {

        // 1) provider 파라미터 파싱 (없으면 kakao 기본)
        String provider = Optional.ofNullable(req.getParameter("provider"))
                .orElse("kakao");

        OauthProviderProperties.Provider cfg = oauthProps.getProviders().get(provider);

        // 2) 잘못된 provider 처리
        if (cfg == null) {
            log.warn("Unknown provider: {}", provider);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown provider");
            return;
        }

        // 3) 리다이렉트 URL 구성
        String redirect = UriComponentsBuilder.fromHttpUrl(cfg.getAuthorizeUri())
                .queryParam("client_id", cfg.getClientId())
                .queryParam("redirect_uri", cfg.getRedirectUri())
                .queryParam("response_type", "code")
                .build()
                .encode()
                .toUriString();

        res.sendRedirect(redirect);
    }

    private static String url(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }
}
