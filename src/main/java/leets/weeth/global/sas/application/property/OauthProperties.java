package leets.weeth.global.sas.application.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "auth")
public class OauthProperties {
    private Map<String, Provider> providers = new HashMap<>();
    private Map<String, RegisteredClient> clients = new HashMap<>();

    @Getter @Setter
    public static class RegisteredClient {
        private String registeredClientId;
        private String clientId;
        private String clientSecret;
        private List<String> redirectUris;
        private List<String> scopes;
        private long accessTokenTtl; // 시간 단위
        private long refreshTokenTtl; // 시간 단위
    }

    @Getter @Setter
    public static class Provider {
        private String authorizeUri;
        private String clientId;
        private String redirectUri;
        private String grantType;
    }
}
