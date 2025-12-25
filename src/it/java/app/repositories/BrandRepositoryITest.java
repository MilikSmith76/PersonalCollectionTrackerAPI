package app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import app.entities.BrandDAO;
import app.generated.types.BrandInput;
import app.test.utils.constants;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@SpringBootTest
@Testcontainers
@Sql("/sql/brand.sql")
public class BrandRepositoryITest {

    @Autowired
    BrandRepository brandRepository;

    @Container
    private static final MySQLContainer mySQLContainer = new MySQLContainer(
        constants.MYSQL_IMAGE
    )
        .withDatabaseName("databasename")
        .withUsername("user")
        .withPassword("password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    private final BrandDAO brand1 = BrandDAO.fromGraphQL(
        BrandInput
            .newBuilder()
            .name("Brand 1")
            .logoUrl("brand_1_logo_url")
            .build()
    );

    private final BrandDAO brand2 = BrandDAO.fromGraphQL(
        BrandInput
            .newBuilder()
            .name("Brand 2")
            .logoUrl("brand_2_logo_url")
            .build()
    );

    @BeforeAll
    static void spinUp() {
        mySQLContainer.start();
    }

    @BeforeEach
    public void setUp() {
        brandRepository.save(brand1);
        brandRepository.save(brand2);
    }

    @AfterAll
    static void spinDown() {
        mySQLContainer.stop();
    }

    @AfterEach
    public void tearDown() {
        brandRepository.deleteAll();
    }

    @Test
    public void shouldFindByIdThenReturnBrand() {
        // Arrange

        // Act
        Optional<BrandDAO> result = brandRepository.findById(1L);

        // Assert
        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().getId()).isEqualTo(brand1.getId());
        assertThat(result.get().getName()).isEqualTo(brand1.getName());
        assertThat(result.get().getLogoUrl()).isEqualTo(brand1.getLogoUrl());
    }
}
