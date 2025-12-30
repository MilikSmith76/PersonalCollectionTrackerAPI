package app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import app.entities.BrandDAO;
import app.generated.types.Brand;
import app.generated.types.BrandFilter;
import app.generated.types.BrandInput;
import app.repositories.BrandRepository;
import app.test.utils.Constants;
import app.test.utils.EntityDaoCreator;
import app.test.utils.SqlFiles;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.AbstractThrowableAssert;
import org.hibernate.PropertyValueException;
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
        brand1 =
        EntityDaoCreator.createBrand(
            Constants.DEFAULT_FIRST_BRAND_NAME,
            Constants.DEFAULT_FIRST_BRAND_LOGO_URL
        );
        brand2 =
        EntityDaoCreator.createBrand(
            Constants.DEFAULT_SECOND_BRAND_NAME,
            Constants.DEFAULT_SECOND_BRAND_LOGO_URL
        );
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
        assertThat(result.isPresent()).isTrue();
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
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(brand2.getId().toString());
        assertThat(result.get().getName()).isEqualTo(brand2.getName());
        assertThat(result.get().getLogoUrl()).isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void findByIdShouldErrorForNonExistentId() {
        // Arrange

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandService.findById(Constants.NON_EXISTENT_ID_STRING);
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage(Constants.NON_EXISTENT_ID_ERROR_MESSAGE);
    }

    @Test
    public void findByCriteriaShouldFindAll() {
        // Arrange

        // Act
        List<Brand> result = brandService.findByCriteria(null);

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
    public void findByCriteriaShouldFilterBrands() {
        // Arrange
        brand2.setDescription(Constants.DEFAULT_DESCRIPTION);
        entityManager.persist(brand2);
        entityManager.flush();

        BrandFilter filter = BrandFilter
            .newBuilder()
            .name(brand2.getName())
            .description(Constants.DEFAULT_DESCRIPTION)
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
        BrandFilter filter = BrandFilter
            .newBuilder()
            .name(Constants.DEFAULT_NON_EXISTENT_BRAND_NAME)
            .build();

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
            .name(Constants.DEFAULT_BRAND_NAME)
            .logoUrl(Constants.DEFAULT_BRAND_LOGO_URL)
            .build();

        // Act
        Optional<Brand> result = brandService.save(input);

        // Assert
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo(input.getName());
        assertThat(result.get().getLogoUrl()).isEqualTo(input.getLogoUrl());
    }

    @Test
    public void saveShouldErrorForInvalidBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .name(Constants.DEFAULT_BRAND_NAME)
            .build();

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandService.save(input);
            });

        // Assert
        result.isInstanceOf(DataIntegrityViolationException.class);
        result.hasMessage(Constants.INVALID_BRAND_ERROR_MESSAGE);
    }

    @Test
    public void updateShouldUpdateBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(brand1.getId().toString())
            .name(String.format("%s Updated", brand1.getName()))
            .logoUrl(brand1.getLogoUrl())
            .description(Constants.DEFAULT_DESCRIPTION)
            .build();

        // Act
        Optional<Brand> result = brandService.update(input, input.getId());

        // Assert
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo(input.getName());
        assertThat(result.get().getDescription())
            .isEqualTo(input.getDescription());
    }

    @Test
    public void updateShouldErrorForNonExistentBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(Constants.NON_EXISTENT_ID_STRING)
            .name(String.format("%s Updated", brand1.getName()))
            .logoUrl(brand1.getLogoUrl())
            .description(Constants.DEFAULT_DESCRIPTION)
            .build();

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandService.update(input, input.getId());
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage(Constants.NON_EXISTENT_ID_ERROR_MESSAGE);
    }

    @Test
    public void updateShouldErrorForInvalidBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(brand1.getId().toString())
            .name(String.format("%s Updated", brand1.getName()))
            .description(Constants.DEFAULT_DESCRIPTION)
            .build();

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandService.update(input, input.getId());
                entityManager.flush();
            });

        // Assert
        result.isInstanceOf(PropertyValueException.class);
        result.hasMessage(Constants.INVALID_BRAND_ERROR_MESSAGE);
    }

    @Test
    public void deleteShouldDeleteBrand() {
        // Arrange

        // Act
        Boolean result = brandService.delete(brand1.getId().toString());

        // Assert
        assertThat(result).isTrue();

        Optional<Brand> updatedBrand = brandService.findById(
            brand1.getId().toString()
        );

        assertThat(updatedBrand.isPresent()).isTrue();
        assertThat(updatedBrand.get().getDeleted()).isTrue();
        assertThat(updatedBrand.get().getDeletedAt()).isNotNull();
    }

    @Test
    public void deleteShouldErrorForNonExistentBrand() {
        // Arrange

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandService.delete(Constants.NON_EXISTENT_ID_STRING);
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage(Constants.NON_EXISTENT_ID_ERROR_MESSAGE);
    }
}
