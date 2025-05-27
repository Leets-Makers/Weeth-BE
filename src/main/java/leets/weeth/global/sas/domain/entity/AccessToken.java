package leets.weeth.global.sas.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.util.Set;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken extends AbstractToken {

    private OAuth2AccessToken.TokenType tokenType;

    private Set<String> scopes;

    private OAuth2TokenFormat tokenFormat;

    private ClaimsHolder claims;

}
