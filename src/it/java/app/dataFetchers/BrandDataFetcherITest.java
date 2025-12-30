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
import app.test.utils.Constants;
import app.test.utils.EntityAttributes;
import app.test.utils.EntityDaoCreator;
import app.test.utils.GqlIntegrationTest;
import app.test.utils.SqlFiles;
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

    private final String createBrandMutationKey = "createBrand";

    private final String updateBrandMutationName = "UpdateBrand";

    private final String updateBrandMutationKey = "updateBrand";

    private final String deleteBrandMutationName = "DeleteBrand";

    private final String deleteBrandMutationKey = "deleteBrand";

    @BeforeEach
    public void setUp() {
        brand1 =
        EntityDaoCreator.createBrand(
            Constants.defaultFirstBrandName,
            Constants.defaultFirstBrandLogoUrl
        );
        brand2 =
        EntityDaoCreator.createBrand(
            Constants.defaultSecondBrandName,
            Constants.defaultSecondBrandLogoUrl
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
        assertThat(result.get(EntityAttributes.ID)).isEqualTo(brand1.getId().toString());
        assertThat(result.get(EntityAttributes.NAME)).isEqualTo(brand1.getName());
        assertThat(result.get(EntityAttributes.LOGO_URL)).isEqualTo(brand1.getLogoUrl());
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
        assertThat(result.get(EntityAttributes.ID)).isEqualTo(brand2.getId().toString());
        assertThat(result.get(EntityAttributes.NAME)).isEqualTo(brand2.getName());
        assertThat(result.get(EntityAttributes.LOGO_URL)).isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void brandQueryShouldReturnErrorForNonExistentId() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandGraphQLQuery.Builder()
                .queryName(brandQueryName)
                .id(Constants.testInvalidIdString)
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
            .isEqualTo(Constants.GRAPH_QL_INTERNAL_ERROR);
        assertThat(result.getFirst().getMessage())
            .isEqualTo(Constants.fullNonExistentIdErrorMessage);
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
        assertThat(result.getFirst().get(EntityAttributes.NAME)).isEqualTo(brand1.getName());
        assertThat(result.getFirst().get(EntityAttributes.LOGO_URL))
            .isEqualTo(brand1.getLogoUrl());
        assertThat(result.getLast().get(EntityAttributes.ID))
            .isEqualTo(brand2.getId().toString());
        assertThat(result.getLast().get(EntityAttributes.NAME)).isEqualTo(brand2.getName());
        assertThat(result.getLast().get(EntityAttributes.LOGO_URL))
            .isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void brandsQueryShouldFilterBrands() {
        // Arrange
        brand2.setDescription(Constants.defaultDescription);
        brandRepository.save(brand2);

        BrandFilter filter = BrandFilter
            .newBuilder()
            .name(Constants.defaultBrandName)
            .description(Constants.defaultDescription)
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
        assertThat(result.getFirst().get(EntityAttributes.NAME)).isEqualTo(brand2.getName());
        assertThat(result.getFirst().get(EntityAttributes.LOGO_URL))
            .isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void brandsQueryShouldFindNothingForFilterWithNoMatches() {
        // Arrange
        BrandFilter filter = BrandFilter
            .newBuilder()
            .description(Constants.defaultDescription)
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
            .name(Constants.defaultBrandName)
            .logoUrl(Constants.defaultBrandLogoUrl)
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new CreateBrandGraphQLQuery.Builder()
                .queryName(createBrandMutationName)
                .input(input)
                .build(),
            new CreateBrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        Map<String, ?> result = gqlExecutor.execute(
            graphQLQueryRequest.serialize(),
            createBrandMutationKey
        );

        // Assert
        assertThat(result.get(EntityAttributes.NAME)).isEqualTo(input.getName());
        assertThat(result.get(EntityAttributes.LOGO_URL)).isEqualTo(input.getLogoUrl());
    }

    @Test
    public void createBrandMutationShouldErrorForInvalidBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .name(Constants.defaultBrandName)
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
            .isEqualTo(Constants.GRAPH_QL_INVALID_ARGUMENT_ERROR);
    }

    @Test
    public void updateBrandMutationShouldUpdateBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(brand2.getId().toString())
            .name(String.format("%s Updated", brand2.getName()))
            .logoUrl(brand2.getLogoUrl())
            .description(Constants.defaultDescription)
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

        // Act
        Map<String, ?> result = gqlExecutor.execute(
            graphQLQueryRequest.serialize(),
            updateBrandMutationKey
        );

        // Assert
        assertThat(result.get(EntityAttributes.ID)).isEqualTo(input.getId());
        assertThat(result.get(EntityAttributes.NAME)).isEqualTo(input.getName());
        assertThat(result.get(EntityAttributes.DESCRIPTION)).isEqualTo(input.getDescription());
    }

    @Test
    public void updateBrandMutationShouldErrorForNonExistentBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(Constants.testInvalidIdString)
            .name(Constants.defaultBrandName)
            .logoUrl(brand2.getLogoUrl())
            .description(Constants.defaultDescription)
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
            .isEqualTo(Constants.GRAPH_QL_INTERNAL_ERROR);
        assertThat(result.getFirst().getMessage())
            .isEqualTo(Constants.fullNonExistentIdErrorMessage);
    }

    @Test
    public void updateBrandMutationShouldErrorForInvalidBrand() {
        // Arrange
        BrandInput input = BrandInput
            .newBuilder()
            .id(brand2.getId().toString())
            .name(String.format("%s Updated", brand2.getName()))
            .description(Constants.defaultDescription)
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
            .isEqualTo(Constants.GRAPH_QL_INVALID_ARGUMENT_ERROR);
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
                .id(Constants.testInvalidIdString)
                .build()
        );

        // Act
        List<GraphQLError> result = gqlExecutor.executeAndReturnErrors(
            graphQLQueryRequest.serialize()
        );

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getErrorType().toString())
            .isEqualTo(Constants.GRAPH_QL_INTERNAL_ERROR);
        assertThat(result.getFirst().getMessage())
            .isEqualTo(Constants.fullNonExistentIdErrorMessage);
    }
}
