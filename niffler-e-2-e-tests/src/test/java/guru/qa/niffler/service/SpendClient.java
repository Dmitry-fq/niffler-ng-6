package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface SpendClient {

    @NonNull
    SpendJson createSpend(SpendJson spend);

    @NonNull
    SpendJson updateSpend(SpendJson spend);

    @NonNull
    CategoryJson createCategory(CategoryJson category);

    @NonNull
    Optional<CategoryJson> findCategoryById(UUID id);

    @NonNull
    Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name);

    @NonNull
    Optional<SpendJson> findSpendById(UUID id);

    @NonNull
    Optional<SpendJson> findByUsernameAndDescription(String username, String description);

    void removeCategory(CategoryJson category);
}
