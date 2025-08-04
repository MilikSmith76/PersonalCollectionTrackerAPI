package app.services;

import app.entities.CardProductTypeDAO;
import app.generated.types.CardProductType;
import app.generated.types.CardProductTypeFilter;
import app.generated.types.CardProductTypeInput;
import app.repositories.CardProductTypeRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardProductTypeService
    extends EntityService<
        CardProductTypeDAO,
        CardProductType,
        CardProductTypeRepository,
        CardProductTypeInput,
        CardProductTypeFilter
    > {

    @Autowired
    public CardProductTypeService(
        CardProductTypeRepository cardProductTypeRepository
    ) {
        super(cardProductTypeRepository);
    }

    @Override
    protected Optional<CardProductType> merge(CardProductTypeInput input) {
        CardProductTypeDAO cardProductTypeDAO = CardProductTypeDAO.fromGraphQL(
            input
        );
        CardProductTypeDAO saved = this.repository.save(cardProductTypeDAO);
        return Optional.of(saved.toGraphQL());
    }
}
