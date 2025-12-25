package app.test.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.mysql.MySQLContainer;

public abstract class IntegrationTest {

    private static final MySQLContainer mySQLContainer = new MySQLContainer(
        Constants.MYSQL_IMAGE
    )
        .withDatabaseName("databasename")
        .withUsername("user")
        .withPassword("password");

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeAll
    public static void spinUp() {
        mySQLContainer.start();
    }

    @AfterAll
    public static void spinDown() {
        mySQLContainer.stop();
    }
}
