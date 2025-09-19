package leets.weeth.global.sas.config;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import leets.weeth.domain.user.domain.entity.SecurityUser;
import leets.weeth.domain.user.domain.service.UserGetService;
import leets.weeth.global.auth.kakao.KakaoAuthService;
import leets.weeth.global.sas.application.exception.Oauth2JwtTokenException;
import leets.weeth.global.sas.application.property.OauthProperties;
import leets.weeth.global.sas.config.authentication.ProviderAwareEntryPoint;
import leets.weeth.global.sas.config.grant.KakaoAccessTokenAuthenticationConverter;
import leets.weeth.global.sas.config.grant.KakaoAuthenticationProvider;
import leets.weeth.global.sas.config.grant.KakaoGrantType;
import leets.weeth.global.sas.domain.repository.OAuth2AuthorizationGrantAuthorizationRepository;
import leets.weeth.global.sas.domain.service.RedisOAuth2AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OAuth2AuthorizationServerConfig {

    @Value("${auth.issuer}")
    private String issuer;

    private final ProviderAwareEntryPoint entryPoint;
    private final KakaoAuthService kakaoAuthService;
    private final UserGetService userGetService;

    private final OauthProperties props;

    private final RSAPublicKey publicKey;
    private final PrivateKey privateKey;

    // SAS용 SecurityFilterChain
    @Bean
    @Order(1) // 우선순위를 기본 filter보다 높게 설정
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      KakaoAccessTokenAuthenticationConverter kakaoConverter,
                                                                      KakaoAuthenticationProvider kakaoProvider) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults())
                .tokenEndpoint(token -> token
                        .accessTokenRequestConverters(c -> c.add(kakaoConverter))
                        .authenticationProviders(p  -> p.add(kakaoProvider))
                );

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        // 커스텀 EntryPoint (provider 파라미터 해석 → 302 /oauth2/authorization/{provider})
        http.exceptionHandling(e -> e.defaultAuthenticationEntryPointFor(
                entryPoint, rq -> rq.getRequestURI().startsWith("/oauth2/authorize")));

        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/oauth2/**", "/.well-known/**"))
                .build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        OauthProperties.RegisteredClient leenk = props.getClients().get("leenk");
        /*
            Leenk Client 등록
         */
        RegisteredClient client = RegisteredClient.withId(leenk.getRegisteredClientId())
                .clientName("LEENK")
                .clientId(leenk.getClientId())
                .clientSecret(leenk.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantTypes(type -> {
                    type.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    type.add(AuthorizationGrantType.REFRESH_TOKEN);
                    type.add(KakaoGrantType.KAKAO_ACCESS_TOKEN);
                })
                .redirectUris(uri -> {
                    uri.addAll(leenk.getRedirectUris());
                })
                .scopes(scope -> {
                    scope.addAll(leenk.getScopes());
                })
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(false)
                        .build())

                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(leenk.getAccessTokenTtl()))
                        .refreshTokenTimeToLive(Duration.ofHours(leenk.getRefreshTokenTtl()))
                        .reuseRefreshTokens(false)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(client);
    }

    @Bean
    @Primary
    public OAuth2AuthorizationService authorizationService(
            RegisteredClientRepository registeredClientRepository,
            OAuth2AuthorizationGrantAuthorizationRepository oAuth2AuthorizationGrantAuthorizationRepository) {

        return new RedisOAuth2AuthorizationService(registeredClientRepository, oAuth2AuthorizationGrantAuthorizationRepository);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    SavedRequestAwareAuthenticationSuccessHandler savedHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler();
    }

    // SAS에서 사용하는 RSA 키쌍을 제공
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = loadRsaKeyFromString();
        return (jwkSelector, context) -> jwkSelector.select(new com.nimbusds.jose.jwk.JWKSet(rsaKey));
    }

    // OIDC 메타데이터에서 issuer를 명시해줘야 client가 자동 설정 가능
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(issuer)
                .build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return ctx -> {
            Object principal = ctx.getPrincipal().getPrincipal();

            if (principal instanceof SecurityUser u) {
                ctx.getClaims().claim("id", u.id());
                ctx.getClaims().claim("email", u.email());
                ctx.getClaims().claim("role", u.role());
            }else {
                throw new Oauth2JwtTokenException();
            }
        };
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    OAuth2TokenGenerator<?> oauth2TokenGenerator(JwtEncoder jwtEncoder) {
        JwtGenerator jwtGenerator               = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(jwtCustomizer());
        OAuth2AccessTokenGenerator accessGen    = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshGen  = new OAuth2RefreshTokenGenerator();

        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessGen, refreshGen);
    }

    @Bean
    KakaoAccessTokenAuthenticationConverter kakaoConverter() {
        return new KakaoAccessTokenAuthenticationConverter();
    }

    @Bean
    KakaoAuthenticationProvider kakaoProvider(
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<?>  tokenGenerator) {

        return new KakaoAuthenticationProvider(
                kakaoAuthService, userGetService, authorizationService, tokenGenerator);
    }

    private RSAKey loadRsaKeyFromString() {
        try {
            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID("weeth-key")
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("RSA 키 파싱 실패", e);
        }
    }
}
