package leets.weeth.global.sas.config;

import leets.weeth.global.sas.application.util.PemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtKeyConfig {

    @Value("${auth.jwt.public-key}")
    private String publicKeyPem;

    @Value("${auth.jwt.private-key}")
    private String privateKeyPem;

    @Bean
    public RSAPublicKey publicKey(PemUtils pemUtils) throws Exception {
        return pemUtils.parsePublicKey(publicKeyPem);
    }

    @Bean
    public PrivateKey privateKey(PemUtils pemUtils) throws Exception {
        return pemUtils.parsePrivateKey(privateKeyPem);
    }
}
