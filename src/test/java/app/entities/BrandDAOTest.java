package app.entities;

import static org.assertj.core.api.Assertions.assertThat;

import app.generated.types.Brand;
import app.generated.types.BrandInput;
import app.test.utils.EntityDaoCreator;
import app.test.utils.TestConstants;
import app.test.utils.TestUtilities;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BrandDAOTest {

    @Test
    public void constructorShouldWork() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(TestConstants.DEFAULT_ID_STRING)
            .name(TestConstants.DEFAULT_BRAND_NAME)
            .logoUrl(TestConstants.DEFAULT_BRAND_LOGO_URL)
            .description(TestConstants.DEFAULT_DESCRIPTION)
            .build();

        // Act
        BrandDAO result = new BrandDAO(input);

        // Assert
        assertThat(result.getId()).isEqualTo(TestConstants.DEFAULT_ID);
        assertThat(result.getName()).isEqualTo(input.getName());
        assertThat(result.getLogoUrl()).isEqualTo(input.getLogoUrl());
        assertThat(result.getDescription()).isEqualTo(input.getDescription());
        assertThat(result.getDeleted()).isFalse();
        assertThat(result.getDeletedAt()).isNull();
    }

    @Test
    public void toGraphQLShouldReturnBrandQL() {
        // Arrange
        BrandDAO brand = EntityDaoCreator.createBrand(
            TestConstants.DEFAULT_BRAND_NAME,
            TestConstants.DEFAULT_BRAND_LOGO_URL
        );
        brand.setId(TestConstants.DEFAULT_ID);
        brand.setDescription(TestConstants.DEFAULT_DESCRIPTION);

        // Act
        Brand result = brand.toGraphQL();

        // Assert
        assertThat(result.getId()).isEqualTo(brand.getId().toString());
        assertThat(result.getName()).isEqualTo(brand.getName());
        assertThat(result.getLogoUrl()).isEqualTo(brand.getLogoUrl());
        assertThat(result.getDescription()).isEqualTo(brand.getDescription());
        assertThat(result.getDeleted()).isFalse();
        assertThat(result.getDeletedAt()).isNull();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void toGraphQLShouldReturnComplexBrandQL() {
        // Arrange
        Date currentTime = TestUtilities.getCurrentTimeStamp();

        BrandDAO brand = EntityDaoCreator.createBrand(
            TestConstants.DEFAULT_BRAND_NAME,
            TestConstants.DEFAULT_BRAND_LOGO_URL
        );
        brand.setId(TestConstants.DEFAULT_ID);
        brand.setDescription(TestConstants.DEFAULT_DESCRIPTION);
        brand.setDeleted(true);
        brand.setDeletedAt(currentTime);
        brand.setCreatedAt(currentTime);
        brand.setUpdatedAt(currentTime);

        // Act
        Brand result = brand.toGraphQL();

        // Assert
        assertThat(result.getId()).isEqualTo(brand.getId().toString());
        assertThat(result.getName()).isEqualTo(brand.getName());
        assertThat(result.getLogoUrl()).isEqualTo(brand.getLogoUrl());
        assertThat(result.getDescription()).isEqualTo(brand.getDescription());
        assertThat(result.getDeleted()).isEqualTo(brand.getDeleted());
        assertThat(result.getDeletedAt()).isEqualTo(brand.getDeletedAtDate());
        assertThat(result.getCreatedAt()).isEqualTo(brand.getCreatedAtDate());
        assertThat(result.getUpdatedAt()).isEqualTo(brand.getUpdatedAtDate());
    }
}
