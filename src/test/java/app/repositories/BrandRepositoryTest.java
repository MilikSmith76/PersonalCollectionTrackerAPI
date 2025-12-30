package app.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import app.entities.BrandDAO;
import app.generated.types.BrandFilter;
import app.test.utils.Constants;
import app.test.utils.EntityDaoCreator;
import app.test.utils.SqlFiles;
import app.test.utils.TestUtilities;
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
public class BrandRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BrandRepository brandRepository;

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
        Optional<BrandDAO> result = brandRepository.findById(brand1.getId());

        // Assert
        assertThat(result.isPresent()).isTrue();
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
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(brand2.getId());
        assertThat(result.get().getName()).isEqualTo(brand2.getName());
        assertThat(result.get().getLogoUrl()).isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void findByIdShouldFindNothingForNonExistentId() {
        // Arrange

        // Act
        Optional<BrandDAO> result = brandRepository.findById(
            Constants.NON_EXISTENT_ID
        );

        // Assert
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void findByCriteriaShouldFindAll() {
        // Arrange

        // Act
        List<BrandDAO> result = brandRepository.findByCriteria(null);

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
    public void findByCriteriaShouldFilterBrands() {
        // Arrange
        brand2.setDescription(Constants.DEFAULT_DESCRIPTION);
        brandRepository.save(brand2);

        BrandFilter filter = BrandFilter
            .newBuilder()
            .name(Constants.DEFAULT_SECOND_BRAND_NAME)
            .description(Constants.DEFAULT_DESCRIPTION)
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
        BrandFilter filter = BrandFilter
            .newBuilder()
            .name(Constants.DEFAULT_NON_EXISTENT_BRAND_NAME)
            .build();

        // Act
        List<BrandDAO> result = brandRepository.findByCriteria(filter);

        // Assert
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void saveShouldCreateNewBrand() {
        // Arrange
        BrandDAO newBrand = EntityDaoCreator.createBrand(
            Constants.DEFAULT_BRAND_NAME,
            Constants.DEFAULT_BRAND_LOGO_URL
        );

        // Act
        BrandDAO result = brandRepository.save(newBrand);

        // Assert
        assertThat(result.getName()).isEqualTo(newBrand.getName());
        assertThat(result.getLogoUrl()).isEqualTo(newBrand.getLogoUrl());
    }

    @Test
    public void saveShouldErrorForInvalidNewBrand() {
        // Arrange
        BrandDAO newBrand = EntityDaoCreator.createBrand(
            Constants.DEFAULT_BRAND_NAME,
            Constants.DEFAULT_BRAND_LOGO_URL
        );
        newBrand.setLogoUrl(null);

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandRepository.save(newBrand);
            });

        // Assert
        result.isInstanceOf(DataIntegrityViolationException.class);
        result.hasMessage(Constants.INVALID_BRAND_ERROR_MESSAGE);
    }

    @Test
    public void saveShouldUpdateBrand() {
        // Arrange
        brand1.setName(String.format("%s Updated", brand1.getName()));
        brand1.setDeleted(true);
        brand1.setDeletedAt(TestUtilities.getCurrentTimeStamp());

        // Act
        BrandDAO result = brandRepository.save(brand1);

        // Assert
        assertThat(result.getName()).isEqualTo(brand1.getName());
        assertThat(result.getDeleted()).isEqualTo(brand1.getDeleted());
        assertThat(result.getDeletedAt()).isEqualTo(brand1.getDeletedAt());
    }

    @Test
    public void saveShouldErrorForInvalidUpdatedBrand() {
        // Arrange
        brand1.setName(String.format("%s Updated", brand1.getName()));
        brand1.setLogoUrl(null);

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                brandRepository.save(brand1);
                brandRepository.flush();
            });

        // Assert
        result.isInstanceOf(DataIntegrityViolationException.class);
        result.hasMessage(Constants.INVALID_BRAND_ERROR_MESSAGE);
    }
}
