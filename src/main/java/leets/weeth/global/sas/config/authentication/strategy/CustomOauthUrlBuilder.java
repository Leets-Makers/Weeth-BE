package leets.weeth.global.sas.config.authentication.strategy;

import leets.weeth.global.sas.application.property.OauthProperties;

public interface CustomOauthUrlBuilder {
    String buildOauthUrl(OauthProperties.Provider cfg);
}
