package leets.weeth.global.sas.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Set;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("oauth2_authorization")
public abstract class OAuth2AuthorizationGrantAuthorization {

    @Id
    private String id;

    private String registeredClientId;

    private String principalName;

    private Set<String> authorizedScopes;

    private AccessToken accessToken;

    private RefreshToken refreshToken;

    @TimeToLive
    private Long expiration;

    public void updateExpire(long ttl) {
        this.expiration = ttl;
    }
}
