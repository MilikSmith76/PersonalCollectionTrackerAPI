package app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import app.entities.BrandDAO;
import app.generated.types.BrandFilter;
import app.test.utils.EntityDaoCreator;
import app.test.utils.SqlFiles;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(SqlFiles.BRAND)
public class BrandRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BrandRepository brandRepository;

    private BrandDAO brand1;

    private BrandDAO brand2;

    @BeforeEach
    public void setUp() {
        brand1 = EntityDaoCreator.createBrand("Brand 1", "brand_1_logo_url");
        brand2 = EntityDaoCreator.createBrand("Brand 2", "brand_2_logo_url");
        entityManager.persist(brand1);
        entityManager.persist(brand2);
        entityManager.flush();
    }

    @AfterEach
    public void tearDown() {
        entityManager.remove(brand1);
        entityManager.remove(brand2);
        entityManager.flush();
    }

    @Test
    public void findByIdShouldFindBrand1() {
        // Arrange

        // Act
        Optional<BrandDAO> result = brandRepository.findById(brand1.getId());

        // Assert
        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().getId()).isEqualTo(brand1.getId());
        assertThat(result.get().getName()).isEqualTo(brand1.getName());
        assertThat(result.get().getLogoUrl()).isEqualTo(brand1.getLogoUrl());
    }

    @Test
    public void findByIdShouldFindBrand2() {
        // Arrange

        // Act
        Optional<BrandDAO> result = brandRepository.findById(brand2.getId());

        // Assert
        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().getId()).isEqualTo(brand2.getId());
        assertThat(result.get().getName()).isEqualTo(brand2.getName());
        assertThat(result.get().getLogoUrl()).isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void findByIdShouldFindNothingForNonExistentId() {
        // Arrange

        // Act
        Optional<BrandDAO> result = brandRepository.findById(0L);

        // Assert
        assertThat(result.isPresent()).isEqualTo(false);
    }

    @Test
    public void findByCriteriaShouldFilterCorrectly() {
        // Arrange
        BrandFilter filter = BrandFilter.newBuilder().name("Brand").build();

        // Act
        List<BrandDAO> result = brandRepository.findByCriteria(filter);

        // Assert
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst().getId()).isEqualTo(brand1.getId());
        assertThat(result.getFirst().getName()).isEqualTo(brand1.getName());
        assertThat(result.getFirst().getLogoUrl())
            .isEqualTo(brand1.getLogoUrl());
        assertThat(result.getLast().getId()).isEqualTo(brand2.getId());
        assertThat(result.getLast().getName()).isEqualTo(brand2.getName());
        assertThat(result.getLast().getLogoUrl())
            .isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void findByCriteriaShouldDoComplexFilterCorrectly() {
        // Arrange
        String description = "This is a test description";
        brand2.setDescription(description);
        brandRepository.save(brand2);

        BrandFilter filter = BrandFilter
            .newBuilder()
            .name("Brand 2")
            .description(description)
            .build();

        // Act
        List<BrandDAO> result = brandRepository.findByCriteria(filter);

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getId()).isEqualTo(brand2.getId());
        assertThat(result.getFirst().getName()).isEqualTo(brand2.getName());
        assertThat(result.getFirst().getLogoUrl())
            .isEqualTo(brand2.getLogoUrl());
        assertThat(result.getFirst().getDescription())
            .isEqualTo(brand2.getDescription());
    }

    @Test
    public void findByCriteriaShouldFindNothingForFilterWithNoMatches() {
        // Arrange
        BrandFilter filter = BrandFilter.newBuilder().name("Brand 3").build();

        // Act
        List<BrandDAO> result = brandRepository.findByCriteria(filter);

        // Assert
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void saveShouldCreateNewBrand() {
        // Arrange
        BrandDAO brand3 = EntityDaoCreator.createBrand(
            "Brand 3",
            "brand_3_logo_url"
        );

        // Act
        BrandDAO result = brandRepository.save(brand3);

        // Assert
        assertThat(result.getName()).isEqualTo(brand3.getName());
        assertThat(result.getLogoUrl()).isEqualTo(brand3.getLogoUrl());
    }

    @Test
    public void saveShouldUpdateBrand() {
        // Arrange
        brand1.setName("Brand 1 Updated");
        brand1.setDeleted(true);
        brand1.setDeletedAt(new Timestamp(System.currentTimeMillis()));

        // Act
        BrandDAO result = brandRepository.save(brand1);

        // Assert
        assertThat(result.getName()).isEqualTo(brand1.getName());
        assertThat(result.getDeleted()).isEqualTo(brand1.getDeleted());
        assertThat(result.getDeletedAt()).isEqualTo(brand1.getDeletedAt());
    }
}
