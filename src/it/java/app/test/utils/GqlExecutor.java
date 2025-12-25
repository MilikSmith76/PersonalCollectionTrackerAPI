package app.test.utils;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import graphql.ExecutionResult;
import java.util.Map;

public class GqlExecutor {

    private final DgsQueryExecutor dgsQueryExecutor;

    public GqlExecutor(DgsQueryExecutor dgsQueryExecutor) {
        this.dgsQueryExecutor = dgsQueryExecutor;
    }

    public Map<String, ?> execute(String query, String queryNameKey) {
        ExecutionResult queryResponse = dgsQueryExecutor.execute(query);

        return queryResponse
            .<Map<String, Map<String, ?>>>getData()
            .get(queryNameKey);
    }
}
