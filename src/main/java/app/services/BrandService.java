package app.services;

import app.entities.BrandDAO;
import app.generated.types.Brand;
import app.generated.types.BrandFilter;
import app.generated.types.BrandInput;
import app.repositories.BrandRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig("brands")
public class BrandService
    extends EntityService<
        BrandDAO,
        Brand,
        BrandRepository,
        BrandInput,
        BrandFilter
    > {

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        super(brandRepository);
    }

    @Override
    protected Optional<Brand> merge(BrandInput input) {
        BrandDAO brandDAO = BrandDAO.fromGraphQL(input);
        BrandDAO savedBrand = this.repository.save(brandDAO);
        return Optional.of(savedBrand.toGraphQL());
    }
}
