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

    @BeforeEach
    public void setUp() {
        brand1 = EntityDaoCreator.createBrand("Brand 1", "brand_1_logo_url");
        brand2 = EntityDaoCreator.createBrand("Brand 2", "brand_2_logo_url");
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
                .queryName("Brand")
                .id(brand1.getId().toString())
                .build(),
            new BrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        Map<String, ?> result = gqlExecutor.execute(
            graphQLQueryRequest.serialize(),
            "brand"
        );

        // Assert
        assertThat(result.get("id")).isEqualTo(brand1.getId().toString());
        assertThat(result.get("name")).isEqualTo(brand1.getName());
        assertThat(result.get("logoUrl")).isEqualTo(brand1.getLogoUrl());
    }

    @Test
    public void brandQueryShouldReturnBrand2() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandGraphQLQuery.Builder()
                .queryName("Brand")
                .id(brand2.getId().toString())
                .build(),
            new BrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        Map<String, ?> result = gqlExecutor.execute(
            graphQLQueryRequest.serialize(),
            "brand"
        );

        // Assert
        assertThat(result.get("id")).isEqualTo(brand2.getId().toString());
        assertThat(result.get("name")).isEqualTo(brand2.getName());
        assertThat(result.get("logoUrl")).isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void brandQueryShouldReturnErrorForNonExistentId() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandGraphQLQuery.Builder().queryName("Brand").id("0").build(),
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
            .isEqualTo(
                "java.lang.IllegalArgumentException: Record with ID 0 does not exist."
            );
    }

    @Test
    public void brandsQueryShouldReturnBrands() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandsGraphQLQuery.Builder().queryName("Brands").build(),
            new BrandsProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        List<Map<String, ?>> result = gqlExecutor.executeAndReturnArray(
            graphQLQueryRequest.serialize(),
            "brands"
        );

        // Assert
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getFirst().get("id"))
            .isEqualTo(brand1.getId().toString());
        assertThat(result.getFirst().get("name")).isEqualTo(brand1.getName());
        assertThat(result.getFirst().get("logoUrl"))
            .isEqualTo(brand1.getLogoUrl());
        assertThat(result.getLast().get("id"))
            .isEqualTo(brand2.getId().toString());
        assertThat(result.getLast().get("name")).isEqualTo(brand2.getName());
        assertThat(result.getLast().get("logoUrl"))
            .isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void brandsQueryShouldFilterSuccessfully() {
        // Arrange
        brand2.setDescription("This is a test.");
        brandRepository.save(brand2);

        BrandFilter filter = BrandFilter
            .newBuilder()
            .name("Brand")
            .description("test")
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandsGraphQLQuery.Builder()
                .queryName("Brands")
                .filter(filter)
                .build(),
            new BrandsProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        List<Map<String, ?>> result = gqlExecutor.executeAndReturnArray(
            graphQLQueryRequest.serialize(),
            "brands"
        );

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().get("id"))
            .isEqualTo(brand2.getId().toString());
        assertThat(result.getFirst().get("name")).isEqualTo(brand2.getName());
        assertThat(result.getFirst().get("logoUrl"))
            .isEqualTo(brand2.getLogoUrl());
    }

    @Test
    public void brandsQueryShouldFindNothingForFilterWithNoMatches() {
        // Arrange
        BrandFilter filter = BrandFilter
            .newBuilder()
            .description("test")
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandsGraphQLQuery.Builder()
                .queryName("Brands")
                .filter(filter)
                .build(),
            new BrandsProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        List<Map<String, ?>> result = gqlExecutor.executeAndReturnArray(
            graphQLQueryRequest.serialize(),
            "brands"
        );

        // Assert
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void createBrandMutationShouldCreateNewBrand() {
        // Arrange
        BrandInput brand3Input = BrandInput
            .newBuilder()
            .name("Brand 3")
            .logoUrl("brand_3_logo_url")
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new CreateBrandGraphQLQuery.Builder()
                .queryName("CreateBrand")
                .input(brand3Input)
                .build(),
            new CreateBrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        Map<String, ?> result = gqlExecutor.execute(
            graphQLQueryRequest.serialize(),
            "createBrand"
        );

        // Assert
        assertThat(result.get("name")).isEqualTo(brand3Input.getName());
        assertThat(result.get("logoUrl")).isEqualTo(brand3Input.getLogoUrl());
    }

    @Test
    public void createBrandMutationShouldErrorForInvalidBrand() {
        // Arrange
        BrandInput brand3Input = BrandInput
            .newBuilder()
            .name("Brand 3")
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new CreateBrandGraphQLQuery.Builder()
                .queryName("CreateBrand")
                .input(brand3Input)
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
        BrandInput updatedBrand2Input = BrandInput
            .newBuilder()
            .id(brand2.getId().toString())
            .name("Brand 2 Updated")
            .logoUrl(brand2.getLogoUrl())
            .description("This is a test.")
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new UpdateBrandGraphQLQuery.Builder()
                .queryName("UpdateBrand")
                .input(updatedBrand2Input)
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
            "updateBrand"
        );

        // Assert
        assertThat(result.get("id")).isEqualTo(updatedBrand2Input.getId());
        assertThat(result.get("name")).isEqualTo(updatedBrand2Input.getName());
        assertThat(result.get("description"))
            .isEqualTo(updatedBrand2Input.getDescription());
    }

    @Test
    public void updateBrandMutationShouldErrorForNonExistentBrand() {
        // Arrange
        BrandInput updatedBrand2Input = BrandInput
            .newBuilder()
            .id("0")
            .name("Invalid Brand")
            .logoUrl(brand2.getLogoUrl())
            .description("This is a test.")
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new UpdateBrandGraphQLQuery.Builder()
                .queryName("UpdateBrand")
                .input(updatedBrand2Input)
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
            .isEqualTo(
                "java.lang.IllegalArgumentException: Record with ID 0 does not exist."
            );
    }

    @Test
    public void updateBrandMutationShouldErrorForInvalidBrand() {
        // Arrange
        BrandInput updatedBrand2Input = BrandInput
            .newBuilder()
            .id("0")
            .name("Invalid Brand")
            .description("This is a test.")
            .build();

        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new UpdateBrandGraphQLQuery.Builder()
                .queryName("UpdateBrand")
                .input(updatedBrand2Input)
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
    public void deleteMutationShouldDeleteBrand() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new DeleteBrandGraphQLQuery.Builder()
                .queryName("DeleteBrand")
                .id(brand2.getId().toString())
                .build()
        );

        // Act
        Boolean result = gqlExecutor.executeAndReturnObject(
            graphQLQueryRequest.serialize(),
            "deleteBrand"
        );

        Optional<BrandDAO> deletedBrand2 = brandRepository.findById(
            brand2.getId()
        );

        // Assert
        assertThat(result).isEqualTo(true);
        assertThat(deletedBrand2.get().getDeleted()).isEqualTo(true);
        assertThat(deletedBrand2.get().getDeletedAt()).isNotNull();
    }

    @Test
    public void deleteMutationShouldErrorForNonExistentBrand() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new DeleteBrandGraphQLQuery.Builder()
                .queryName("DeleteBrand")
                .id("0")
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
            .isEqualTo(
                "java.lang.IllegalArgumentException: Record with ID 0 does not exist."
            );
    }
}
