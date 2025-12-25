package app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import app.entities.BrandDAO;
import app.generated.types.BrandInput;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/sql/brand.sql")
public class BrandRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BrandRepository brandRepository;

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

    @BeforeEach
    public void setUp() {
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
