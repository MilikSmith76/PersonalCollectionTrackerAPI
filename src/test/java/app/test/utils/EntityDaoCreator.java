package app.test.utils;

import app.entities.BrandDAO;
import app.generated.types.BrandInput;

public class EntityDaoCreator {

    public static BrandDAO createBrand(String brandName, String logoUrl) {
        return BrandDAO.fromGraphQL(
            BrandInput.newBuilder().name(brandName).logoUrl(logoUrl).build()
        );
    }
}
