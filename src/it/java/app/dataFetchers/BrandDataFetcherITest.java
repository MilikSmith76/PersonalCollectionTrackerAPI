package app.dataFetchers;

import static org.assertj.core.api.Assertions.assertThat;

import app.entities.BrandDAO;
import app.generated.client.BrandGraphQLQuery;
import app.generated.client.BrandProjectionRoot;
import app.generated.client.BrandsGraphQLQuery;
import app.generated.client.BrandsProjectionRoot;
import app.generated.client.CreateBrandGraphQLQuery;
import app.generated.client.CreateBrandProjectionRoot;
import app.generated.client.DeleteBrandGraphQLQuery;
import app.generated.client.UpdateBrandGraphQLQuery;
import app.generated.client.UpdateBrandProjectionRoot;
import app.generated.types.BrandFilter;
import app.generated.types.BrandInput;
import app.repositories.BrandRepository;
import app.test.utils.EntityAttributes;
import app.test.utils.EntityDaoCreator;
import app.test.utils.GqlIntegrationTest;
import app.test.utils.SqlFiles;
import app.test.utils.TestConstants;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import graphql.GraphQLError;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@EnableDgsTest
@Testcontainers
@Sql(SqlFiles.BRAND)
public class BrandDataFetcherITest extends GqlIntegrationTest {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    public BrandDataFetcherITest(DgsQueryExecutor dgsQueryExecutor) {
        super(dgsQueryExecutor);
    }

    private BrandDAO brand1;

    private BrandDAO brand2;

    private final String brandQueryName = "Brand";

    private final String brandQueryKey = "brand";

    private final String brandsQueryName = "Brands";

    private final String brandsQueryKey = "brands";

    private final String createBrandMutationName = "CreateBrand";

    private final String updateBrandMutationName = "UpdateBrand";

    private final String deleteBrandMutationName = "DeleteBrand";

    @BeforeEach
    public void setUp() {
        brand1 =
        EntityDaoCreator.createBrand(
            TestConstants.DEFAULT_FIRST_BRAND_NAME,
            TestConstants.DEFAULT_FIRST_BRAND_LOGO_URL
        );
        brand2 =
        EntityDaoCreator.createBrand(
            TestConstants.DEFAULT_SECOND_BRAND_NAME,
            TestConstants.DEFAULT_SECOND_BRAND_LOGO_URL
        );
        brandRepository.save(brand1);
        brandRepository.save(brand2);
        brandRepository.flush();
    }

    @AfterEach
    public void tearDown() {
        brandRepository.deleteAll();
        brandRepository.flush();
    }

    @Test
    public void brandQueryShouldReturnBrand1() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandGraphQLQuery.Builder()
                .queryName(brandQueryName)
                .id(brand1.getId().toString())
                .build(),
            new BrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        Map<String, ?> result = gqlExecutor.execute(
            graphQLQueryRequest.serialize(),
            brandQueryKey
        );

        // Assert
        assertThat(result.get(EntityAttributes.ID))
            .isEqualTo(brand1.getId().toString());
        assertThat(result.get(EntityAttributes.NAME))
            .isEqualTo(brand1.getName());
        assertThat(result.get(EntityAttributes.LOGO_URL))
            .isEqualTo(brand1.getLogoUrl());
    }

    @Test
    public void brandQueryShouldReturnBrand2() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandGraphQLQuery.Builder()
                .queryName(brandQueryName)
                .id(brand2.getId().toString())
                .build(),
            new BrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        Map<String, ?> result = gqlExecutor.execute(
            graphQLQueryRequest.serialize(),
            brandQueryKey
        );

        // Assert
        assertThat(result.get(EntityAttributes.ID))
            .isEqualTo(brand2.getId().toString());
        assertThat(result.get(EntityAttributes.NAME))
            .isEqualTo(brand2.getName());
        assertThat(result.get(EntityAttributes.LOGO_URL))
            .isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void brandQueryShouldReturnErrorForNonExistentId() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandGraphQLQuery.Builder()
                .queryName(brandQueryName)
                .id(TestConstants.NON_EXISTENT_ID_STRING)
                .build(),
            new BrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        List<GraphQLError> result = gqlExecutor.executeAndReturnErrors(
            graphQLQueryRequest.serialize()
        );

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getErrorType().toString())
            .isEqualTo(TestConstants.GRAPH_QL_INTERNAL_ERROR);
        assertThat(result.getFirst().getMessage())
            .isEqualTo(TestConstants.FULL_NON_EXISTENT_ID_ERROR_MESSAGE);
    }

    @Test
    public void brandsQueryShouldReturnBrands() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandsGraphQLQuery.Builder().queryName(brandsQueryName).build(),
            new BrandsProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        List<Map<String, ?>> result = gqlExecutor.executeAndReturnArray(
            graphQLQueryRequest.serialize(),
            brandsQueryKey
        );

        // Assert
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst().get(EntityAttributes.ID))
            .isEqualTo(brand1.getId().toString());
        assertThat(result.getFirst().get(EntityAttributes.NAME))
            .isEqualTo(brand1.getName());
        assertThat(result.getFirst().get(EntityAttributes.LOGO_URL))
            .isEqualTo(brand1.getLogoUrl());
        assertThat(result.getLast().get(EntityAttributes.ID))
            .isEqualTo(brand2.getId().toString());
        assertThat(result.getLast().get(EntityAttributes.NAME))
            .isEqualTo(brand2.getName());
        assertThat(result.getLast().get(EntityAttributes.LOGO_URL))
            .isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void brandsQueryShouldFilterBrands() {
        // Arrange
        brand2.setDescription(TestConstants.DEFAULT_DESCRIPTION);
        brandRepository.save(brand2);

        BrandFilter filter = BrandFilter
            .newBuilder()
            .name(TestConstants.DEFAULT_BRAND_NAME)
            .description(TestConstants.DEFAULT_DESCRIPTION)
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandsGraphQLQuery.Builder()
                .queryName(brandQueryName)
                .filter(filter)
                .build(),
            new BrandsProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        List<Map<String, ?>> result = gqlExecutor.executeAndReturnArray(
            graphQLQueryRequest.serialize(),
            brandsQueryKey
        );

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().get(EntityAttributes.ID))
            .isEqualTo(brand2.getId().toString());
        assertThat(result.getFirst().get(EntityAttributes.NAME))
            .isEqualTo(brand2.getName());
        assertThat(result.getFirst().get(EntityAttributes.LOGO_URL))
            .isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void brandsQueryShouldFindNothingForFilterWithNoMatches() {
        // Arrange
        BrandFilter filter = BrandFilter
            .newBuilder()
            .description(TestConstants.DEFAULT_DESCRIPTION)
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandsGraphQLQuery.Builder()
                .queryName(brandsQueryName)
                .filter(filter)
                .build(),
            new BrandsProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        List<Map<String, ?>> result = gqlExecutor.executeAndReturnArray(
            graphQLQueryRequest.serialize(),
            brandsQueryKey
        );

        // Assert
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void createBrandMutationShouldCreateNewBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .name(TestConstants.DEFAULT_BRAND_NAME)
            .logoUrl(TestConstants.DEFAULT_BRAND_LOGO_URL)
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new CreateBrandGraphQLQuery.Builder()
                .queryName(createBrandMutationName)
                .input(input)
                .build(),
            new CreateBrandProjectionRoot<>().id().name().logoUrl()
        );

        String createBrandMutationKey = "createBrand";

        // Act
        Map<String, ?> result = gqlExecutor.execute(
            graphQLQueryRequest.serialize(),
            createBrandMutationKey
        );

        // Assert
        assertThat(result.get(EntityAttributes.NAME))
            .isEqualTo(input.getName());
        assertThat(result.get(EntityAttributes.LOGO_URL))
            .isEqualTo(input.getLogoUrl());
    }

    @Test
    public void createBrandMutationShouldErrorForInvalidBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .name(TestConstants.DEFAULT_BRAND_NAME)
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new CreateBrandGraphQLQuery.Builder()
                .queryName(createBrandMutationName)
                .input(input)
                .build(),
            new CreateBrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        List<GraphQLError> result = gqlExecutor.executeAndReturnErrors(
            graphQLQueryRequest.serialize()
        );

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getErrorType().toString())
            .isEqualTo(TestConstants.GRAPH_QL_INVALID_ARGUMENT_ERROR);
    }

    @Test
    public void updateBrandMutationShouldUpdateBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(brand2.getId().toString())
            .name(String.format("%s Updated", brand2.getName()))
            .logoUrl(brand2.getLogoUrl())
            .description(TestConstants.DEFAULT_DESCRIPTION)
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new UpdateBrandGraphQLQuery.Builder()
                .queryName(updateBrandMutationName)
                .input(input)
                .build(),
            new UpdateBrandProjectionRoot<>()
                .id()
                .name()
                .logoUrl()
                .description()
        );

        String updateBrandMutationKey = "updateBrand";

        // Act
        Map<String, ?> result = gqlExecutor.execute(
            graphQLQueryRequest.serialize(),
            updateBrandMutationKey
        );

        // Assert
        assertThat(result.get(EntityAttributes.ID)).isEqualTo(input.getId());
        assertThat(result.get(EntityAttributes.NAME))
            .isEqualTo(input.getName());
        assertThat(result.get(EntityAttributes.DESCRIPTION))
            .isEqualTo(input.getDescription());
    }

    @Test
    public void updateBrandMutationShouldErrorForNonExistentBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(TestConstants.NON_EXISTENT_ID_STRING)
            .name(TestConstants.DEFAULT_BRAND_NAME)
            .logoUrl(brand2.getLogoUrl())
            .description(TestConstants.DEFAULT_DESCRIPTION)
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new UpdateBrandGraphQLQuery.Builder()
                .queryName(updateBrandMutationName)
                .input(input)
                .build(),
            new UpdateBrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        List<GraphQLError> result = gqlExecutor.executeAndReturnErrors(
            graphQLQueryRequest.serialize()
        );

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getErrorType().toString())
            .isEqualTo(TestConstants.GRAPH_QL_INTERNAL_ERROR);
        assertThat(result.getFirst().getMessage())
            .isEqualTo(TestConstants.FULL_NON_EXISTENT_ID_ERROR_MESSAGE);
    }

    @Test
    public void updateBrandMutationShouldErrorForInvalidBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(brand2.getId().toString())
            .name(String.format("%s Updated", brand2.getName()))
            .description(TestConstants.DEFAULT_DESCRIPTION)
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new UpdateBrandGraphQLQuery.Builder()
                .queryName(updateBrandMutationName)
                .input(input)
                .build(),
            new UpdateBrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        List<GraphQLError> result = gqlExecutor.executeAndReturnErrors(
            graphQLQueryRequest.serialize()
        );

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getErrorType().toString())
            .isEqualTo(TestConstants.GRAPH_QL_INVALID_ARGUMENT_ERROR);
    }

    @Test
    public void deleteBrandMutationShouldDeleteBrand() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new DeleteBrandGraphQLQuery.Builder()
                .queryName(deleteBrandMutationName)
                .id(brand2.getId().toString())
                .build()
        );

        String deleteBrandMutationKey = "deleteBrand";

        // Act
        Boolean result = gqlExecutor.executeAndReturnObject(
            graphQLQueryRequest.serialize(),
            deleteBrandMutationKey
        );

        // Assert
        assertThat(result).isTrue();

        Optional<BrandDAO> deletedBrand2 = brandRepository.findById(
            brand2.getId()
        );

        assertThat(deletedBrand2.isPresent()).isTrue();
        assertThat(deletedBrand2.get().getDeleted()).isTrue();
        assertThat(deletedBrand2.get().getDeletedAt()).isNotNull();
    }

    @Test
    public void deleteBrandMutationShouldErrorForNonExistentBrand() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new DeleteBrandGraphQLQuery.Builder()
                .queryName(deleteBrandMutationName)
                .id(TestConstants.NON_EXISTENT_ID_STRING)
                .build()
        );

        // Act
        List<GraphQLError> result = gqlExecutor.executeAndReturnErrors(
            graphQLQueryRequest.serialize()
        );

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getErrorType().toString())
            .isEqualTo(TestConstants.GRAPH_QL_INTERNAL_ERROR);
        assertThat(result.getFirst().getMessage())
            .isEqualTo(TestConstants.FULL_NON_EXISTENT_ID_ERROR_MESSAGE);
    }
}
