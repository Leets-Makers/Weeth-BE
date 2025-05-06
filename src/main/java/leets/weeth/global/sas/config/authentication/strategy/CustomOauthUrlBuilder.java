package leets.weeth.global.sas.config.authentication.strategy;

import leets.weeth.global.sas.application.property.OauthProviderProperties;

public interface CustomOauthUrlBuilder {
    String buildOauthUrl(OauthProviderProperties.Provider cfg);
}
