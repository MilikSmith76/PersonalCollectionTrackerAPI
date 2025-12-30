package app.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import app.entities.BrandDAO;
import app.generated.types.Brand;
import app.test.utils.EntityDaoCreator;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilitiesTest {

    @Test
    public void isNumberShouldReturnTrueForNumber() {
        // Arrange
        String value = "12345";

        // Act
        boolean result = Utilities.isNumber(value);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isNumberShouldReturnFalseForAlphaNum() {
        // Arrange
        String value = "0a1b2c6";

        // Act
        boolean result = Utilities.isNumber(value);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isNumberShouldReturnFalseForLetters() {
        // Arrange
        String value = "test";

        // Act
        boolean result = Utilities.isNumber(value);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isNumberShouldReturnFalseForNull() {
        // Arrange
        String value = null;

        // Act
        boolean result = Utilities.isNumber(value);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void getIdOrNullShouldReturnIdForNumber() {
        // Arrange
        String value = "12345";

        // Act
        Long result = Utilities.getIdOrNull(value);

        // Assert
        assertThat(result).isEqualTo(12345L);
    }

    @Test
    public void getIdOrNullShouldReturnNullForNonNumber() {
        // Arrange
        String value = "0a1b2c6";

        // Act
        Long result = Utilities.getIdOrNull(value);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void getIdOrNullShouldReturnNullForLetters() {
        // Arrange
        String value = "0a1b2c6";

        // Act
        Long result = Utilities.getIdOrNull(value);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void getIdOrNullShouldReturnNullForNull() {
        // Arrange
        String value = null;

        // Act
        Long result = Utilities.getIdOrNull(value);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void getEntityGQLOrNullShouldReturnEntityQLForEntity() {
        // Arrange
        BrandDAO entity = EntityDaoCreator.createBrand(
            "Brand",
            "brand_logo_url"
        );

        // Act
        Brand result = Utilities.getEntityGQLOrNull(entity);

        // Assert
        assertThat(result.getName()).isEqualTo(entity.getName());
        assertThat(result.getLogoUrl()).isEqualTo(entity.getLogoUrl());
    }

    @Test
    public void getEntityGQLOrNullShouldReturnNullForNull() {
        // Arrange
        BrandDAO entity = null;

        // Act
        Brand result = Utilities.getEntityGQLOrNull(entity);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void toGraphQLIdShouldReturnIdForId() {
        // Arrange
        Long id = 1L;

        // Act
        String result = Utilities.toGraphQLId(id);

        // Assert
        assertThat(result).isEqualTo("1");
    }

    @Test
    public void toGraphQLIdShouldReturnNullForNull() {
        // Arrange
        Long id = null;

        // Act
        String result = Utilities.toGraphQLId(id);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void fromGraphQLIdShouldReturnIdForNumber() {
        // Arrange
        String value = "12345";

        // Act
        Long result = Utilities.fromGraphQLId(value);

        // Assert
        assertThat(result).isEqualTo(12345L);
    }

    @Test
    public void fromGraphQLIdShouldErrorForNonNumber() {
        // Arrange
        String value = "0a1b2c6";

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                Utilities.fromGraphQLId(value);
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Invalid ID format: " + value);
    }

    @Test
    public void fromGraphQLIdShouldErrorForLetters() {
        // Arrange
        String value = "0a1b2c6";

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                Utilities.fromGraphQLId(value);
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Invalid ID format: " + value);
    }

    @Test
    public void fromGraphQLIdShouldErrorForNull() {
        // Arrange
        String value = null;

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                Utilities.fromGraphQLId(value);
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Invalid ID format: " + value);
    }
}
