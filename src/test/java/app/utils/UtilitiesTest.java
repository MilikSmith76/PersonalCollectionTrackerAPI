package app.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import app.entities.BrandDAO;
import app.generated.types.Brand;
import app.test.utils.Constants;
import app.test.utils.EntityDaoCreator;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilitiesTest {

    private final String numberString = "12345";

    private final String alphaNumString = "0a1b2c6";

    private final String lettersString = "test";

    private final String nullString = null;

    @Test
    public void isNumberShouldReturnTrueForNumber() {
        // Arrange

        // Act
        boolean result = Utilities.isNumber(numberString);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void isNumberShouldReturnFalseForAlphaNum() {
        // Arrange

        // Act
        boolean result = Utilities.isNumber(alphaNumString);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isNumberShouldReturnFalseForLetters() {
        // Arrange

        // Act
        boolean result = Utilities.isNumber(lettersString);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void isNumberShouldReturnFalseForNull() {
        // Arrange

        // Act
        boolean result = Utilities.isNumber(nullString);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void getIdOrNullShouldReturnIdForNumber() {
        // Arrange

        // Act
        Long result = Utilities.getIdOrNull(numberString);

        // Assert
        assertThat(result).isEqualTo(12345L);
    }

    @Test
    public void getIdOrNullShouldReturnNullForAlphaNum() {
        // Arrange

        // Act
        Long result = Utilities.getIdOrNull(alphaNumString);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void getIdOrNullShouldReturnNullForLetters() {
        // Arrange

        // Act
        Long result = Utilities.getIdOrNull(lettersString);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void getIdOrNullShouldReturnNullForNull() {
        // Arrange

        // Act
        Long result = Utilities.getIdOrNull(nullString);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void getEntityGQLOrNullShouldReturnEntityQLForEntity() {
        // Arrange
        BrandDAO entity = EntityDaoCreator.createBrand(
            Constants.defaultBrandName,
            Constants.defaultBrandLogoUrl
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

        // Act
        String result = Utilities.toGraphQLId(Constants.testId);

        // Assert
        assertThat(result).isEqualTo(Constants.testIdString);
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

        // Act
        Long result = Utilities.fromGraphQLId(numberString);

        // Assert
        assertThat(result).isEqualTo(Long.parseLong(numberString));
    }

    @Test
    public void fromGraphQLIdShouldErrorForAlphaNum() {
        // Arrange

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                Utilities.fromGraphQLId(alphaNumString);
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Invalid ID format: " + alphaNumString);
    }

    @Test
    public void fromGraphQLIdShouldErrorForLetters() {
        // Arrange

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                Utilities.fromGraphQLId(lettersString);
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Invalid ID format: " + lettersString);
    }

    @Test
    public void fromGraphQLIdShouldErrorForNull() {
        // Arrange

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> {
                Utilities.fromGraphQLId(nullString);
            });

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Invalid ID format: " + nullString);
    }
}
