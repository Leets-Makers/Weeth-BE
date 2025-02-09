package leets.weeth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

//@OpenAPIDefinition(servers = {
//        @Server(url="https://api.weeth.site", description = "Default Api Server url"),
//        @Server(url="http://localhost:8080", description = "Local Api url")
//})
@EnableScheduling
@EnableJpaAuditing
@EnableWebSecurity
@SpringBootApplication
public class WeethApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeethApplication.class, args);
    }

}
