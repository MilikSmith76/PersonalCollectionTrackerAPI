package app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import app.entities.BrandDAO;
import app.test.utils.EntityDaoCreator;
import app.test.utils.IntegrationTest;
import app.test.utils.SqlFiles;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Sql(SqlFiles.BRAND)
public class BrandRepositoryITest extends IntegrationTest {

    @Autowired
    private BrandRepository brandRepository;

    private BrandDAO brand1;

    private BrandDAO brand2;

    @BeforeEach
    public void setUp() {
        brand1 = EntityDaoCreator.createBrand("Brand 1", "brand_1_logo_url");
        brand2 = EntityDaoCreator.createBrand("Brand 2", "brand_2_logo_url");
        brandRepository.save(brand1);
        brandRepository.save(brand2);
        brandRepository.flush();
    }

    @AfterEach
    public void tearDown() {
        brandRepository.deleteAll();
        brandRepository.flush();
    }

    @Test
    public void shouldFindByIdThenReturnBrand() {
        // Arrange

        // Act
        Optional<BrandDAO> result = brandRepository.findById(brand1.getId());

        // Assert
        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().getId()).isEqualTo(brand1.getId());
        assertThat(result.get().getName()).isEqualTo(brand1.getName());
        assertThat(result.get().getLogoUrl()).isEqualTo(brand1.getLogoUrl());
    }
}
