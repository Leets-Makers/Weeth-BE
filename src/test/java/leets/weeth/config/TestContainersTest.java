package leets.weeth.config;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.mysql.MySQLContainer;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import(TestContainersConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TestContainersTest {

    @Autowired
    private MySQLContainer mysqlContainer;

    @Test
    void 설정파일로_주입된_컨테이너_정상_동작_테스트() {
        assertThat(mysqlContainer).isNotNull();
        assertThat(mysqlContainer.isRunning()).isTrue();
        System.out.println("Container JDBC URL: " + mysqlContainer.getJdbcUrl());
    }
}
