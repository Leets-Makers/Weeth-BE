package leets.weeth.global.sas.config.authentication.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OauthUrlBuilderResolver {
    private final Map<String, CustomOauthUrlBuilder> builders;

    public CustomOauthUrlBuilder resolve(String provider) {
        return builders.get(provider.toLowerCase());
    }
}
