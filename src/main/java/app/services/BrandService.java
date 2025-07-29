package app.services;

import app.entities.BrandDAO;
import app.generated.types.Brand;
import app.generated.types.BrandFilter;
import app.generated.types.BrandInput;
import app.repositories.BrandRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Optional<Brand> findById(Long id) {
        return this.brandRepository.findById(id).map(BrandDAO::toGraphQL);
    }

    public List<Brand> findByCriteria(BrandFilter filter) {
        return this.brandRepository.findByCriteria(filter)
            .stream()
            .map(BrandDAO::toGraphQL)
            .collect(Collectors.toList());
    }

    private Optional<Brand> merge(BrandInput input) {
        BrandDAO brandDAO = new BrandDAO(input);
        BrandDAO savedBrand = this.brandRepository.save(brandDAO);
        return Optional.of(savedBrand.toGraphQL());
    }

    public Optional<Brand> save(BrandInput input) {
        return this.merge(input);
    }

    private void validateId(Long id) throws IllegalArgumentException {
        if (!brandRepository.existsById(id)) {
            throw new IllegalArgumentException(
                "Brand with ID " + id + " does not exist."
            );
        }
    }

    public Optional<Brand> update(BrandInput input) {
        if (input.getId() == null) {
            throw new IllegalArgumentException(
                "Brand ID must not be null for update."
            );
        }

        this.validateId(Long.valueOf(input.getId()));

        return this.merge(input);
    }

    public Boolean delete(Long id) {
        this.validateId(id);

        brandRepository.deleteById(id);
        return true;
    }
}
