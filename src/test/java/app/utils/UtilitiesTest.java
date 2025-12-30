package app.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import app.entities.BrandDAO;
import app.generated.types.Brand;
import app.test.utils.EntityDaoCreator;
import app.test.utils.TestConstants;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilitiesTest {

    private final String numberString = "12345";

    private final String alphaNumString = "0a1b2c6";

    private final String lettersString = "test";

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
        boolean result = Utilities.isNumber(null);

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
        Long result = Utilities.getIdOrNull(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void getEntityGQLOrNullShouldReturnEntityQLForEntity() {
        // Arrange
        BrandDAO entity = EntityDaoCreator.createBrand(
            TestConstants.DEFAULT_BRAND_NAME,
            TestConstants.DEFAULT_BRAND_LOGO_URL
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

        // Act
        Brand result = Utilities.getEntityGQLOrNull(null);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void toGraphQLIdShouldReturnIdForId() {
        // Arrange

        // Act
        String result = Utilities.toGraphQLId(TestConstants.DEFAULT_ID);

        // Assert
        assertThat(result).isEqualTo(TestConstants.DEFAULT_ID_STRING);
    }

    @Test
    public void toGraphQLIdShouldReturnNullForNull() {
        // Arrange

        // Act
        String result = Utilities.toGraphQLId(null);

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
            assertThatThrownBy(() -> Utilities.fromGraphQLId(alphaNumString));

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Invalid ID format: " + alphaNumString);
    }

    @Test
    public void fromGraphQLIdShouldErrorForLetters() {
        // Arrange

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> Utilities.fromGraphQLId(lettersString));

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Invalid ID format: " + lettersString);
    }

    @Test
    public void fromGraphQLIdShouldErrorForNull() {
        // Arrange

        // Act
        AbstractThrowableAssert<?, ? extends Throwable> result =
            assertThatThrownBy(() -> Utilities.fromGraphQLId(null));

        // Assert
        result.isInstanceOf(IllegalArgumentException.class);
        result.hasMessage("Invalid ID format: " + null);
    }
}
