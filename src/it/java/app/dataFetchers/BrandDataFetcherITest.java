package app.dataFetchers;

import static org.assertj.core.api.Assertions.assertThat;

import app.entities.BrandDAO;
import app.generated.client.BrandGraphQLQuery;
import app.generated.client.BrandProjectionRoot;
import app.generated.types.BrandInput;
import app.repositories.BrandRepository;
import app.test.utils.constants;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import graphql.ExecutionResult;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@SpringBootTest
@EnableDgsTest
@Testcontainers
@Sql("/sql/brand.sql")
public class BrandDataFetcherITest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    BrandRepository brandRepository;

    @Container
    private static final MySQLContainer mySQLContainer = new MySQLContainer(
        constants.MYSQL_IMAGE
    )
        .withDatabaseName("databasename")
        .withUsername("user")
        .withPassword("password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

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

    @BeforeAll
    static void spinUp() {
        mySQLContainer.start();
    }

    @BeforeEach
    public void setUp() {
        brandRepository.save(brand1);
        brandRepository.save(brand2);
        brandRepository.flush();
    }

    @AfterAll
    static void spinDown() {
        mySQLContainer.stop();
    }

    @AfterEach
    public void tearDown() {
        brandRepository.deleteAll();
    }

    @Test
    public void shouldQueryBrandThenReturnBrand() {
        // Arrange
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
            new BrandGraphQLQuery.Builder().queryName("Brand").id("1").build(),
            new BrandProjectionRoot<>().id().name().logoUrl()
        );

        // Act
        ExecutionResult runQuery = dgsQueryExecutor.execute(
            graphQLQueryRequest.serialize()
        );

        Map<String, ?> result = runQuery
            .<Map<String, Map<String, ?>>>getData()
            .get("brand");

        // Assert
        assertThat(result.get("id")).isEqualTo(brand1.getId().toString());
        assertThat(result.get("name")).isEqualTo(brand1.getName());
        assertThat(result.get("logoUrl")).isEqualTo(brand1.getLogoUrl());
    }
}
