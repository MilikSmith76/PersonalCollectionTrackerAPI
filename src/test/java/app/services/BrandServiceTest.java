package app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import app.entities.BrandDAO;
import app.generated.types.Brand;
import app.generated.types.BrandFilter;
import app.generated.types.BrandInput;
import app.repositories.BrandRepository;
import app.test.utils.EntityDaoCreator;
import app.test.utils.SqlFiles;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(SqlFiles.BRAND)
public class BrandServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    private final BrandService brandService;

    @Autowired
    public BrandServiceTest(BrandRepository brandRepository) {
        brandService = new BrandService(brandRepository);
    }

    private BrandDAO brand1;

    private BrandDAO brand2;

    @BeforeEach
    public void setUp() {
        brand1 = EntityDaoCreator.createBrand("Brand 1", "brand_1_logo_url");
        brand2 = EntityDaoCreator.createBrand("Brand 2", "brand_2_logo_url");
        entityManager.persist(brand1);
        entityManager.persist(brand2);
    }

    @AfterEach
    public void tearDown() {
        entityManager.remove(brand1);
        entityManager.remove(brand2);
    }

    @Test
    public void findByIdShouldFindBrand1() {
        // Arrange

        // Act
        Optional<Brand> result = brandService.findById(
            brand1.getId().toString()
        );

        // Assert
        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().getId()).isEqualTo(brand1.getId().toString());
        assertThat(result.get().getName()).isEqualTo(brand1.getName());
        assertThat(result.get().getLogoUrl()).isEqualTo(brand1.getLogoUrl());
    }

    @Test
    public void findByIdShouldFindBrand2() {
        // Arrange

        // Act
        Optional<Brand> result = brandService.findById(
            brand2.getId().toString()
        );

        // Assert
        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().getId()).isEqualTo(brand2.getId().toString());
        assertThat(result.get().getName()).isEqualTo(brand2.getName());
        assertThat(result.get().getLogoUrl()).isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void findByIdShouldFindNothingForNonExistentId() {
        // Arrange

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandService.findById("0");
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Record with ID 0 does not exist.");
    }

    @Test
    public void findByCriteriaShouldFilterCorrectly() {
        // Arrange
        BrandFilter filter = BrandFilter.newBuilder().name("Brand").build();

        // Act
        List<Brand> result = brandService.findByCriteria(filter);

        // Assert
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst().getId())
            .isEqualTo(brand1.getId().toString());
        assertThat(result.getFirst().getName()).isEqualTo(brand1.getName());
        assertThat(result.getFirst().getLogoUrl())
            .isEqualTo(brand1.getLogoUrl());
        assertThat(result.getLast().getId())
            .isEqualTo(brand2.getId().toString());
        assertThat(result.getLast().getName()).isEqualTo(brand2.getName());
        assertThat(result.getLast().getLogoUrl())
            .isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void findByCriteriaShouldDoComplexFilterCorrectly() {
        // Arrange
        String description = "This is a test description";
        brand2.setDescription(description);
        entityManager.persist(brand2);
        entityManager.flush();

        BrandFilter filter = BrandFilter
            .newBuilder()
            .name("Brand 2")
            .description(description)
            .build();

        // Act
        List<Brand> result = brandService.findByCriteria(filter);

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getId())
            .isEqualTo(brand2.getId().toString());
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
        List<Brand> result = brandService.findByCriteria(filter);

        // Assert
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void saveShouldCreateNewBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .name("Brand 3")
            .logoUrl("brand_3_logo_url")
            .build();

        // Act
        Optional<Brand> result = brandService.save(input);

        // Assert
        assertThat(result.get().getName()).isEqualTo(input.getName());
        assertThat(result.get().getLogoUrl()).isEqualTo(input.getLogoUrl());
    }

    @Test
    public void saveShouldErrorForInvalidBrand() {
        // Arrange
        BrandInput input = BrandInput.newBuilder().name("Brand 3").build();

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandService.save(input);
            });

        // Assert
        result.isInstanceOf(DataIntegrityViolationException.class);
        result.hasMessage(
            "not-null property references a null or transient value: app.entities.BrandDAO.logoUrl"
        );
    }

    @Test
    public void updateShouldUpdateBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(brand1.getId().toString())
            .name("Brand 1 Updated")
            .logoUrl(brand1.getLogoUrl())
            .description("This is a test description")
            .build();

        // Act
        Optional<Brand> result = brandService.update(input, input.getId());

        // Assert
        assertThat(result.get().getName()).isEqualTo(input.getName());
        assertThat(result.get().getDescription())
            .isEqualTo(input.getDescription());
    }

    @Test
    public void updateShouldErrorForNonExistentBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id("0")
            .name("Brand 1 Updated")
            .logoUrl(brand1.getLogoUrl())
            .description("This is a test description")
            .build();

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandService.update(input, input.getId());
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Record with ID 0 does not exist.");
    }

    @Test
    public void deleteShouldDeleteBrand() {
        // Arrange

        // Act
        Boolean result = brandService.delete(brand1.getId().toString());
        Optional<Brand> updatedBrand = brandService.findById(
            brand1.getId().toString()
        );

        // Assert
        assertThat(result).isEqualTo(true);
        assertThat(updatedBrand.get().getDeleted()).isEqualTo(true);
        assertThat(updatedBrand.get().getDeletedAt()).isNotNull();
    }

    @Test
    public void deleteShouldErrorForNonExistentBrand() {
        // Arrange

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandService.delete("0");
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Record with ID 0 does not exist.");
    }
}
