package app.entities;

import static org.assertj.core.api.Assertions.assertThat;

import app.generated.types.Brand;
import app.generated.types.BrandInput;
import app.test.utils.EntityDaoCreator;
import java.sql.Timestamp;
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
            .id("1")
            .name("Brand")
            .logoUrl("brand_logo_url")
            .description("This is a description.")
            .build();

        // Act
        BrandDAO result = new BrandDAO(input);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(input.getName());
        assertThat(result.getLogoUrl()).isEqualTo(input.getLogoUrl());
        assertThat(result.getDescription()).isEqualTo(input.getDescription());
        assertThat(result.getDeleted()).isEqualTo(false);
        assertThat(result.getDeletedAt()).isNull();
    }

    @Test
    public void toGraphQLShouldReturnBrandQL() {
        // Arrange
        BrandDAO brand = EntityDaoCreator.createBrand(
            "Brand",
            "brand_logo_url"
        );
        brand.setId(1L);
        brand.setDescription("This is a description.");

        // Act
        Brand result = brand.toGraphQL();

        // Assert
        assertThat(result.getId()).isEqualTo(brand.getId().toString());
        assertThat(result.getName()).isEqualTo(brand.getName());
        assertThat(result.getLogoUrl()).isEqualTo(brand.getLogoUrl());
        assertThat(result.getDescription()).isEqualTo(brand.getDescription());
        assertThat(result.getDeleted()).isEqualTo(false);
        assertThat(result.getDeletedAt()).isNull();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    public void toGraphQLShouldReturnComplexBrandQL() {
        // Arrange
        Date currentTime = new Timestamp(System.currentTimeMillis());

        BrandDAO brand = EntityDaoCreator.createBrand(
            "Brand",
            "brand_logo_url"
        );
        brand.setId(1L);
        brand.setDescription("This is a description.");
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
