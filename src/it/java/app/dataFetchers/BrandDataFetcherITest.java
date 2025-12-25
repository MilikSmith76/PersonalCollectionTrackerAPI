package app.dataFetchers;

import static org.assertj.core.api.Assertions.assertThat;

import app.entities.BrandDAO;
import app.generated.client.BrandGraphQLQuery;
import app.generated.client.BrandProjectionRoot;
import app.repositories.BrandRepository;
import app.test.utils.EntityDaoCreator;
import app.test.utils.GqlIntegrationTest;
import app.test.utils.SqlFiles;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import java.util.Map;
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
    }

    @Test
    public void shouldQueryBrandThenReturnBrand() {
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
}
