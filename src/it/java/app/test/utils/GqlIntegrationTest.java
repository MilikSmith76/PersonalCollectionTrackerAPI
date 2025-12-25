package app.test.utils;

import com.netflix.graphql.dgs.DgsQueryExecutor;

public abstract class GqlIntegrationTest extends IntegrationTest {

    protected GqlExecutor gqlExecutor;

    public GqlIntegrationTest(DgsQueryExecutor dgsQueryExecutor) {
        gqlExecutor = new GqlExecutor(dgsQueryExecutor);
    }
}
