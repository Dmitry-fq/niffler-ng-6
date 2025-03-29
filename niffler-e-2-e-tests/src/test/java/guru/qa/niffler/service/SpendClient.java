package guru.qa.niffler.service;

import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendClient {

    @Nullable
    SpendJson createSpend(SpendJson spend);

    @Nullable
    SpendJson updateSpend(SpendJson spend);

    @Nullable
    CategoryJson createCategory(CategoryJson category);

    @Nonnull
    Optional<CategoryJson> findCategoryById(UUID id);

    @Nonnull
    Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name);

    @Nonnull
    Optional<SpendJson> findSpendById(UUID id);

    @Nonnull
    Optional<SpendJson> findByUsernameAndDescription(String username, String description);

    void removeCategory(CategoryJson category);
}
