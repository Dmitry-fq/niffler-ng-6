package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendClient {

    @Nonnull
    SpendJson createSpend(SpendJson spend);

    @Nonnull
    SpendJson updateSpend(SpendJson spend);

    @Nonnull
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
