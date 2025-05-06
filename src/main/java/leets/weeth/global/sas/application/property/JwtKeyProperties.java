package leets.weeth.global.sas.application.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtKeyProperties {
    private String publicKey;
    private String privateKey;
}

