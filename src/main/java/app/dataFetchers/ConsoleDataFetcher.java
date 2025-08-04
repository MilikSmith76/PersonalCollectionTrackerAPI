package app.dataFetchers;

import app.generated.types.Console;
import app.generated.types.ConsoleFilter;
import app.generated.types.ConsoleInput;
import app.services.ConsoleService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class ConsoleDataFetcher {

    private final ConsoleService consoleService;

    @Autowired
    public ConsoleDataFetcher(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }

    @DgsQuery
    public Optional<Console> console(@InputArgument String id) {
        return this.consoleService.findById(id);
    }

    @DgsQuery
    public List<Console> consoles(@InputArgument ConsoleFilter filter) {
        return this.consoleService.findByCriteria(filter);
    }

    @DgsMutation
    public Optional<Console> createConsole(@InputArgument ConsoleInput input) {
        return this.consoleService.save(input);
    }

    @DgsMutation
    public Optional<Console> updateConsole(@InputArgument ConsoleInput input) {
        return this.consoleService.update(input, input.getId());
    }

    @DgsMutation
    public Boolean deleteConsole(@InputArgument String id) {
        return consoleService.delete(id);
    }
}
