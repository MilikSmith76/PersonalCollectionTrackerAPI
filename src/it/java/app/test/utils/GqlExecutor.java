package app.test.utils;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import java.util.List;
import java.util.Map;

public class GqlExecutor {

    private final DgsQueryExecutor dgsQueryExecutor;

    public GqlExecutor(DgsQueryExecutor dgsQueryExecutor) {
        this.dgsQueryExecutor = dgsQueryExecutor;
    }

    private <T> T executeHelper(String query, String queryNameKey) {
        ExecutionResult queryResponse = dgsQueryExecutor.execute(query);

        return queryResponse.<Map<String, T>>getData().get(queryNameKey);
    }

    public Map<String, ?> execute(String query, String queryNameKey) {
        return this.executeHelper(query, queryNameKey);
    }

    public List<Map<String, ?>> executeAndReturnArray(
        String query,
        String queryNameKey
    ) {
        return this.executeHelper(query, queryNameKey);
    }

    public <T> T executeAndReturnObject(String query, String queryNameKey) {
        return this.executeHelper(query, queryNameKey);
    }

    public List<GraphQLError> executeAndReturnErrors(String query) {
        ExecutionResult queryResponse = dgsQueryExecutor.execute(query);

        return queryResponse.getErrors();
    }
}
