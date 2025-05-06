package leets.weeth.global.sas.config;

import leets.weeth.global.sas.application.property.JwtKeyProperties;
import leets.weeth.global.sas.application.util.PemUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@RequiredArgsConstructor
public class JwtKeyConfig {

    private final JwtKeyProperties jwtKeyProperties;

    @Bean
    public RSAPublicKey publicKey(PemUtils pemUtils) throws Exception {
        return pemUtils.parsePublicKey(jwtKeyProperties.getPublicKey());
    }

    @Bean
    public PrivateKey privateKey(PemUtils pemUtils) throws Exception {
        return pemUtils.parsePrivateKey(jwtKeyProperties.getPrivateKey());
    }
}
